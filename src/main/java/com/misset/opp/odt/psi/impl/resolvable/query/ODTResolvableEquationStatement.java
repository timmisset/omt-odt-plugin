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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
                .map(resource -> traverseResource(resource,
                        (ODTResolvableQueryPath) leftHand,
                        (ODTResolvableQueryPath) rightHand))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<OntResource> traverseResource(OntResource resource,
                                              ODTResolvableQueryPath left,
                                              ODTResolvableQueryPath right) {
        // to pass the filter, resolve the filter left-hand and right-hand and
        // make sure at least 1 item overlaps
        final Set<OntResource> fromSet = Set.of(resource);
        Set<OntResource> rightSide = right.resolveFromSet(fromSet);
        Set<OntResource> leftSide = left.resolveFromSet(fromSet);

        // check if the filter is compatible based on the direct result
        if (leftSide.stream().anyMatch(rightSide::contains)) {
            return Set.of(resource);
        }

        // not a direct match, there might be match based on class types. If the left-side is a subclass of the right-side
        // or the other way around, this is also acceptable. It is similar to 'Class instance X' and then casting it to the more
        // specific class rather than the generic super class type it had before the filter
        if (leftSide.stream().allMatch(OppModel.INSTANCE::isClass) &&
                rightSide.stream().allMatch(OppModel.INSTANCE::isClass)) {
            Set<OntClass> ontClasses = intersectBothSideSuperClasses(OppModel.INSTANCE.toClasses(leftSide), OppModel.INSTANCE.toClasses(rightSide));
            if (resource.isIndividual()) {
                return OppModel.INSTANCE.toIndividuals(ontClasses);
            } else {
                return Collections.unmodifiableSet(ontClasses);
            }
        }
        return Collections.emptySet();
    }

    /*
        Helper method to resolve type filtering with superclass input
     */
    private Set<OntClass> intersectBothSideSuperClasses(Set<OntClass> leftSide, Set<OntClass> rightSide) {
        HashSet<OntClass> validClasses = new HashSet<>();
        validClasses.addAll(intersectSuperClasses(leftSide, rightSide));
        validClasses.addAll(intersectSuperClasses(rightSide, leftSide));
        return validClasses;
    }

    private Set<OntClass> intersectSuperClasses(Set<OntClass> sideA, Set<OntClass> sideB) {
        return sideA.stream()
                .map(OntClass.class::cast)
                .filter(leftResource -> {
                    Set<OntClass> superClasses = OppModel.INSTANCE.getSuperClasses(leftResource);
                    return sideB.stream().anyMatch(superClasses::contains);
                })
                .collect(Collectors.toSet());
    }

    @Override
    public Predicate<Set<OntResource>> getTypeFilter(PsiElement element) {
        if (OppModel.INSTANCE.isUpdating()) {
            return resources -> false;
        }
        ODTQuery leftSide = getQueryList().get(0);
        ODTQuery rightSide = getQueryList().get(1);
        ODTQuery opposite = PsiTreeUtil.isAncestor(leftSide, element, true) ? leftSide : rightSide;
        Set<OntResource> oppositeResources = opposite.resolve();

        return resources -> resources.isEmpty() || oppositeResources.isEmpty() ||
                OppModel.INSTANCE.areCompatible(oppositeResources, resources);
    }
}
