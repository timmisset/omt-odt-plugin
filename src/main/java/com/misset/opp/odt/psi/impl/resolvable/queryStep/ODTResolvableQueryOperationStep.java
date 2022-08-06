package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.ODTQueryArrayImpl;
import com.misset.opp.odt.psi.impl.callable.ODTBaseDefineQueryStatement;
import com.misset.opp.odt.psi.impl.resolvable.ODTBaseResolvable;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQuery;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQueryPath;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The ODTQueryOperation is the container of a ODTQueryStep and one or more filters
 * It is the class that should be used to resolve the query step since it contains the filter
 */
public abstract class ODTResolvableQueryOperationStep extends ODTBaseResolvable implements ODTQueryOperationStep, ODTResolvable {
    public ODTResolvableQueryOperationStep(@NotNull ASTNode node) {
        super(node);
    }

    private static final Key<CachedValue<Set<OntResource>>> RESOLVED_VALUE = new Key<>("RESOLVED_VALUE");
    private static final Key<CachedValue<Set<OntResource>>> RESOLVED_WITHOUT_FILTER_VALUE = new Key<>("RESOLVED_WITHOUT_FILTER_VALUE");
    private static final Logger LOGGER = Logger.getInstance(ODTResolvableQueryOperationStep.class);

    @Override
    public ODTResolvableQueryPath getParent() {
        return (ODTResolvableQueryPath) super.getParent();
    }

    protected boolean isFirstStepInPath() {
        return this.equals(getParent().getQueryOperationStepList().get(0));
    }

    protected boolean isRootStep() {
        return getParent().startsWithDelimiter() && isFirstStepInPath();
    }

    public Set<OntResource> resolvePreviousStep() {
        return resolvePreviousStep(Collections.emptySet(), null);
    }

    public Set<OntResource> resolvePreviousStep(Set<OntResource> resources,
                                                @Nullable PsiCall call) {
        if (isFirstStepInPath()) {
            // the fromSet is to support filtering overrides
            // each OntResource going into the filter is evaluated with the filter to determine if it survives
            final Set<OntResource> fromSet = getParent().getFromSet();
            if (fromSet != null) {
                return fromSet;
            }
            // check if inside container, in which case, resolve to the step preceding the container
            // for example:
            // /ont:ClassA / ^rdf:type[rdf:type == /ont:ClassA]
            // the rdf:type in the filter should return the outcome of the ^rdf:type
            return Optional.ofNullable(PsiTreeUtil.getParentOfType(this,
                            ODTQueryFilter.class,
                            ODTSignature.class,
                            ODTResolvableSubQueryStep.class,
                            ODTQueryArray.class,
                            ODTBaseDefineQueryStatement.class))
                    .map(container -> resolvePreviousStep(container, resources, call))
                    .orElse(Collections.emptySet());
        } else {
            final List<ODTResolvableQueryOperationStep> queryOperationList = getParent().getResolvableQueryOperationStepList();
            int index = queryOperationList.indexOf(this);
            return queryOperationList.get(index - 1).resolve();
        }
    }

    /**
     * Based on the container, a certain behavior is required to determine the input
     * For example:
     * - ODTQueryFilter: resolve the step without the filter, then filter the outcome
     * - Signature: return the previous step, if non it will repeat the unwrapping until finally it returns an EmptySet
     * or a valid Set could be returned
     */
    private Set<OntResource> resolvePreviousStep(PsiElement container,
                                                 Set<OntResource> resources,
                                                 @Nullable PsiCall call) {
        if (container instanceof ODTBaseDefineQueryStatement) {
            return ((ODTBaseDefineQueryStatement) container).getBase();
        }
        final ODTResolvableQueryOperationStep queryOperationStep = getResolvableStepForContainer(container);
        if (queryOperationStep == null) {
            return Collections.emptySet();
        }
        if (container instanceof ODTQueryFilter) {
            // resolve the queryStep, without filter, so it can be passed into the filter:
            return queryOperationStep.resolveWithoutFilter();
        }
        // else, repeat the process of either unwrapping from a container or resolving the previous step in the same path
        return queryOperationStep.resolvePreviousStep(resources, call);
    }

    private ODTResolvableQueryOperationStep getResolvableStepForContainer(PsiElement container) {
        if (container instanceof ODTQueryArray) {
            ODTQuery odtQuery = ((ODTQueryArrayImpl) container).getQueryList().get(0);
            if (PsiTreeUtil.isAncestor(odtQuery, this, true)) {
                return PsiTreeUtil.getParentOfType(odtQuery, ODTResolvableQueryOperationStep.class);
            } else {
                return PsiTreeUtil.getParentOfType(
                        PsiTreeUtil.getDeepestLast(odtQuery), ODTResolvableQueryOperationStep.class);
            }
        } else {
            return PsiTreeUtil.getParentOfType(container, ODTResolvableQueryOperationStep.class);
        }
    }

    public Set<OntResource> resolveWithoutFilter() {
        return CachedValuesManager.getCachedValue(this, RESOLVED_WITHOUT_FILTER_VALUE, () -> {
            Set<OntResource> resources = Optional.ofNullable(getQueryStep())
                    .map(ODTResolvable::resolve)
                    .orElse(Collections.emptySet());
            return getODTFile()
                    .getCachedValue(resources,
                            OppModel.ONTOLOGY_MODEL_MODIFICATION_TRACKER);
        });
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        super.inspect(holder);
        Set<OntResource> resolve = resolve();
        Set<OntResource> withoutFilter = resolveWithoutFilter();
        if (resolve.isEmpty() && !withoutFilter.isEmpty()) {
            // the filter removed all input:
            holder.registerProblem(this, "No items left after filtering", ProblemHighlightType.WARNING);
        }
    }

    /**
     * Tries to resolve the included filter(s) for this query-step which will reduce
     * the possible resources in the final result.
     * Only works checks types filters and includes negation
     */
    public Set<OntResource> filter(Set<OntResource> resources) {
        final List<ODTQueryFilter> queryFilterList = getQueryFilterList();
        if (queryFilterList.isEmpty()) {
            return resources;
        }
        for (ODTQueryFilter filter : queryFilterList) {
            resources = filter(resources, filter);
        }
        return resources;
    }

    private Set<OntResource> filter(Set<OntResource> resources,
                                    ODTQueryFilter filter) {
        return LoggerUtil.computeWithLogger(LOGGER, "Filtering resources", () -> {
            // range selection filtering has no impact on the performance and
            // can be done without the settings check
            if (filter.getRangeSelection() != null) {
                return resources;
            }

            final ODTQuery query = filter.getQuery();
            if (query instanceof ODTResolvableQuery) {
                return query.filter(resources);
            }
            return resources;
        });
    }

    private boolean isPartOfFilter() {
        return PsiTreeUtil.getParentOfType(this, ODTQueryFilter.class) != null;
    }

    /**
     * Generic method to resolve the calculated value from the cache or calculate it
     * Should only be overridden if the ODTQueryStep implementation should cache the value due to some external
     * dependency outside the default scope of this element
     */
    @Override
    public @NotNull Set<OntResource> resolve() {
        return LoggerUtil.computeWithLogger(LOGGER, "Resolving " + getText(), () -> {
            if (getQueryStep() != null) {
                return filter(getQueryStep().resolve());
            } else {
                return Collections.emptySet();
            }
        });
    }

    @Override
    public @NotNull Set<OntResource> resolve(Context context) {
        return LoggerUtil.computeWithLogger(LOGGER, "Resolving with resources: " + getText(),
                () -> Optional.ofNullable(getQueryStep())
                        .map(queryStep -> queryStep.resolve(context))
                        .orElse(Collections.emptySet()));
    }
}
