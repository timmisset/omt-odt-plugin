package com.misset.opp.odt.builtin.operators;

import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class IdentityOperator extends BuiltInOperator {
    private IdentityOperator() { }
    public static final IdentityOperator INSTANCE = new IdentityOperator();

    @Override
    public String getName() {
        return "IDENTITY";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources) {
        return resources;
    }
}
