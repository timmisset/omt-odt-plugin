package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Key;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTQueryFilter;
import com.misset.opp.odt.psi.ODTQueryOperation;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQuery;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQueryPath;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        if(isFirstStepInPath()) {
            final Set<OntResource> fromSet = getParent().getFromSet();
            if(fromSet != null) {
                return fromSet;
            }
            // check if inside container, in which case, resolve to the step preceding the container
            // for example:
            // /ont:ClassA / ^rdf:type[rdf:type == /ont:ClassA]
            // the rdf:type in the filter should return the outcome of the ^rdf:type
            return Optional.ofNullable(PsiTreeUtil.getParentOfType(this, ODTQueryFilter.class))
                    .map(container -> PsiTreeUtil.getParentOfType(container, ODTResolvableQueryOperationStep.class))
                    .map(ODTResolvableQueryOperationStep::resolveWithoutFilter)
                    .orElse(Collections.emptySet());
        } else {
            final List<ODTResolvableQueryOperationStep> queryOperationList = getParent().getResolvableQueryOperationStepList();
            int index = queryOperationList.indexOf(this);
            return queryOperationList.get(index -1).resolve();
        }
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
        if(queryFilterList.isEmpty()) { return resources; }
        for(ODTQueryFilter filter : queryFilterList) {
            resources = filter(resources, filter);
        }
        return resources;
    }
    private Set<OntResource> filter(Set<OntResource> resources, ODTQueryFilter filter) {
        final ODTQuery query = filter.getQuery();
        if(query instanceof ODTResolvableQuery) {
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
    public Set<OntResource> resolve() {
        if(getQueryStep() != null) {
            if(isPartOfFilter()) {
                // Filters are evaluated more often to determine which of the input resources
                // survive the filter. Therefore, caching the outcome of one would mean they
                // all either pass or fail.
                return filter(getQueryStep().resolve());
            } else {
                return CachedValuesManager.getCachedValue(this,
                        RESOLVED_VALUE,
                        () -> new CachedValueProvider.Result<>(filter(getQueryStep().resolve()), PsiModificationTracker.MODIFICATION_COUNT));
            }
        } else {
            return Collections.emptySet();
        }
    }
}
