package com.misset.opp.callable.builtin.operators;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

public class TraverseOperator extends BuiltInOperator {
    private TraverseOperator() { }
    public static final TraverseOperator INSTANCE = new TraverseOperator();

    @Override
    public String getName() {
        return "TRAVERSE";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.OWL_THING;
    }
}
