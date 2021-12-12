package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTEquationStatement;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class ODTResolvableEquationStatement extends ODTResolvableQuery implements ODTEquationStatement,
        ODTTypeFilterProvider {
    public ODTResolvableEquationStatement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
    }

    @Override
    public Set<OntResource> filter(Set<OntResource> resources) {
        // possibility: $input[rdf:type == /ont:ClassA] or $input[/ont:ClassA == rdf:type]
        // more complexity is not supported
        final ODTQuery leftHand = getQueryList().get(0);
        final ODTQuery rightHand = getQueryList().get(1);

        // only an equation statement with 2 query paths can be analysed
        if (!(leftHand instanceof ODTResolvableQueryPath && rightHand instanceof ODTResolvableQueryPath)) {
            return resources;
        }

        return resources.stream()
                .filter(resource -> traverseResource(resource,
                        (ODTResolvableQueryPath) leftHand,
                        (ODTResolvableQueryPath) rightHand))
                .collect(Collectors.toSet());
    }

    private boolean traverseResource(OntResource resource,
                                     ODTResolvableQueryPath left,
                                     ODTResolvableQueryPath right) {
        final Set<OntResource> fromSet = Set.of(resource);
        final Set<OntResource> intersect = new HashSet<>(toClass(left.resolveFromSet(fromSet)));
        intersect.retainAll(toClass(right.resolveFromSet(fromSet)));
        return !intersect.isEmpty();
    }

    private Set<OntClass> toClass(Set<OntResource> resources) {
        return resources.stream()
                .map(this::toClass)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private OntClass toClass(OntResource resource) {
        if (resource.isClass()) {
            return resource.asClass();
        }
        if (resource.isIndividual()) {
            return resource.asIndividual().getOntClass();
        }
        return null;
    }


    @Override
    public Predicate<Set<OntResource>> getTypeFilter(PsiElement element) {
        ODTQuery leftSide = getQueryList().get(0);
        ODTQuery rightSide = getQueryList().get(1);
        ODTQuery opposite = PsiTreeUtil.isAncestor(leftSide, element, true) ? leftSide : rightSide;
        Set<OntResource> oppositeResources = opposite.resolve();

        return resources -> resources.isEmpty() || oppositeResources.isEmpty() ||
                OppModel.INSTANCE.areCompatible(oppositeResources, resources);
    }
}
