package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTEquationStatement;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ODTResolvableEquationStatement extends ODTResolvableQuery implements ODTEquationStatement {
    public ODTResolvableEquationStatement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> resolve() {
        return Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
    }

    @Override
    public Set<OntResource> filter(Set<OntResource> resources) {
        // possibility: $input[rdf:type == /ont:ClassA] or $input[/ont:ClassA == rdf:type]
        // more complexity is not supported
        final ODTQuery leftHand = getQueryList().get(0);
        final ODTQuery rightHand = getQueryList().get(1);
        if(leftHand == null || rightHand == null) { return resources; }

        // create an intersect of left and righthand
        final Set<OntResource> intersect = new HashSet<>(leftHand.resolve());
        intersect.retainAll(rightHand.resolve());

        final Set<OntResource> filter = intersect.stream().filter(OntResource::isClass).collect(Collectors.toSet());
        return resources.stream().filter(resource -> filterResource(resource, filter)).collect(Collectors.toSet());
    }
    private boolean filterResource(OntResource resource, Set<OntResource> filter) {
        if(resource.isClass()) {
            return filter.contains(resource);
        } else if (resource instanceof Individual) {
            return filter.contains(resource.asIndividual().getOntClass());
        } else {
            // if confused, don't filter
            return true;
        }
    }
}
