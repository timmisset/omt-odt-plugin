package com.misset.opp.callable.builtin.operators;

import com.misset.opp.odt.psi.impl.call.ODTCall;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public class IfEmptyOperator extends BuiltInOperator {
    private IfEmptyOperator() { }
    public static final IfEmptyOperator INSTANCE = new IfEmptyOperator();

    @Override
    public String getName() {
        return "IF_EMPTY";
    }

    /**
     * The IF_EMPTY operator will either return the input resources or, when empty, return the provided value
     * At compilation both types are possible
     */
    @Override
    public Set<OntResource> resolveFrom(Set<OntResource> resources,
                                        ODTCall call) {
        final HashSet<OntResource> ontResources = new HashSet<>(resources);
        ontResources.addAll(call.resolveSignatureArgument(0));
        return ontResources;
    }
}
