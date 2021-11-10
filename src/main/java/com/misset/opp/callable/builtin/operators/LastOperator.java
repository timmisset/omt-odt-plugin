package com.misset.opp.callable.builtin.operators;

import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class LastOperator extends BuiltInOperator {
    private LastOperator() { }
    public static final LastOperator INSTANCE = new LastOperator();

    @Override
    public String getName() {
        return "LAST";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources) {
        return resources;
    }
}
