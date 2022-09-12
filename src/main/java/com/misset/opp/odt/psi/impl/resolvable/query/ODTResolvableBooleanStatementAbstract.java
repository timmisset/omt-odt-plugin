package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTBooleanStatement;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.impl.ODTTypeFilterProvider;
import com.misset.opp.odt.psi.resolvable.query.ODTResolvableQuery;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public abstract class ODTResolvableBooleanStatementAbstract extends ODTResolvableQueryAbstract implements
        ODTBooleanStatement,
        ODTTypeFilterProvider {
    protected ODTResolvableBooleanStatementAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Set.of(OppModelConstants.getXsdBooleanInstance());
    }

    @Override
    public Set<OntResource> filter(Set<OntResource> resources) {
        /*
            // possibility: $input[rdf:type == /ont:ClassA OR rdf:type == /ont:ClassB]
            // possibility: $input[rdf:type == /ont:ClassA AND rdf:type == /ont:ClassB] <-- that would be a weird filter
         */
        final List<ODTQuery> queryList = getQueryList();
        Set<OntResource> filteredResources = new HashSet<>(queryList.get(0).filter(resources));
        for (ODTQuery query : queryList.subList(1, queryList.size() - 1)) {
            final PsiElement psiElement = PsiTreeUtil.prevVisibleLeaf(query);
            if (psiElement != null && psiElement.getNode().getElementType() == ODTTypes.BOOLEAN_OPERATOR) {
                final String operator = psiElement.getText();
                if ("AND".equals(operator)) {
                    // intersect:
                    filteredResources.retainAll(query.filter(resources));
                } else if ("OR".equals(operator)) {
                    filteredResources.addAll(query.filter(resources));
                }
            }
        }
        return filteredResources;
    }

    @Override
    public Predicate<Set<OntResource>> getTypeFilter(PsiElement element) {
        return resources -> resources.isEmpty() || resources.contains(OppModelConstants.getXsdBooleanInstance());
    }

    @Override
    public boolean requiresInput() {
        return getQueryList().stream().anyMatch(ODTResolvableQuery::requiresInput);
    }
}
