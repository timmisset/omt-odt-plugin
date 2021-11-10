package com.misset.opp.callable.builtin.operators;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

public class ErrorOperator extends BuiltInOperator {
    private ErrorOperator() { }
    public static final ErrorOperator INSTANCE = new ErrorOperator();

    @Override
    public String getName() {
        return "ERROR";
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
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.ERROR;
    }
}
