package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTQueryFilter;
import com.misset.opp.odt.psi.ODTQueryOperation;
import com.misset.opp.odt.psi.ODTSignature;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQuery;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQueryPath;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The ODTQueryOperation is the container of a ODTQueryStep and one or more filters
 * It is the class that should be used to resolve the query step since it contains the filter
 */
public abstract class ODTResolvableQueryOperationStep extends ASTWrapperPsiElement implements ODTQueryOperation, ODTResolvable {
    public ODTResolvableQueryOperationStep(@NotNull ASTNode node) {
        super(node);
    }

    private static final Key<CachedValue<Set<OntResource>>> RESOLVED_VALUE = new Key<>("RESOLVED_VALUE");

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
            return Optional.ofNullable(PsiTreeUtil.getParentOfType(this, ODTQueryFilter.class, ODTSignature.class))
                    .map(this::resolvePreviousStep)
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
    private Set<OntResource> resolvePreviousStep(PsiElement container) {
        final ODTResolvableQueryOperationStep queryOperationStep = PsiTreeUtil.getParentOfType(container,
                ODTResolvableQueryOperationStep.class);
        if (queryOperationStep == null) {
            return Collections.emptySet();
        }
        if (container instanceof ODTQueryFilter) {
            // resolve the queryStep, without filter, so it can be passed into the filter:
            return queryOperationStep.resolveWithoutFilter();
        } else if (container instanceof ODTDefineStatement) {
            /*
                todo
                allow for a @base annotation to determine the leading item
             */
        }
        // else, repeat the process of either unwrapping from a container or resolving the previous step in the same path
        return queryOperationStep.resolvePreviousStep();
    }

    public Set<OntResource> resolveWithoutFilter() {
        return Optional.ofNullable(getQueryStep())
                .map(ODTResolvable::resolve)
                .orElse(Collections.emptySet());
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
        final ODTQuery query = filter.getQuery();
        if (query instanceof ODTResolvableQuery) {
            return query.filter(resources);
        }
        return resources;
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
        if (getQueryStep() != null) {
            if (isPartOfFilter()) {
                // Filters are evaluated more often to determine which of the input resources
                // survive the filter. Therefore, caching the outcome of one would mean they
                // all either pass or fail.
                return filter(getQueryStep().resolve());
            } else {
                return CachedValuesManager.getCachedValue(this,
                        RESOLVED_VALUE,
                        () -> new CachedValueProvider.Result<>(filter(getQueryStep().resolve()),
                                PsiModificationTracker.MODIFICATION_COUNT, this, OppModel.ONTOLOGY_MODEL_MODIFICATION));
            }
        } else {
            return Collections.emptySet();
        }
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        /*
         * Inspection should visit the QueryStep directly
         */
    }

    @Override
    public void annotate(AnnotationHolder holder) {
        /*
         * Annotator should visit the QueryStep directly
         */
    }
}
