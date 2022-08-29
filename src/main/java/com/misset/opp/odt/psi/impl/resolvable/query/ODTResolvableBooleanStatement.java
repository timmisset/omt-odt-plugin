package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTBooleanStatement;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class ODTResolvableBooleanStatement extends ODTResolvableQuery implements ODTBooleanStatement, ODTTypeFilterProvider {
    protected ODTResolvableBooleanStatement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }

    @Override
    public Set<OntResource> filter(Set<OntResource> resources) {
        /*
            // possibility: $input[rdf:type == /ont:ClassA OR rdf:type == /ont:ClassB]
            // possibility: $input[rdf:type == /ont:ClassA AND rdf:type == /ont:ClassB] <-- that would be a weird filter
         */
        final List<ODTResolvableQuery> queryList = getQueryList().stream().map(ODTResolvableQuery.class::cast).collect(
                Collectors.toList());
        Set<OntResource> filteredResources = new HashSet<>(queryList.get(0).filter(resources));
        for (ODTResolvableQuery query : queryList.subList(1, queryList.size() - 1)) {
            final PsiElement psiElement = PsiTreeUtil.prevVisibleLeaf(query);
            if(psiElement != null && psiElement.getNode().getElementType() == ODTTypes.BOOLEAN_OPERATOR) {
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
        return resources -> resources.isEmpty() || resources.contains(OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }
}
