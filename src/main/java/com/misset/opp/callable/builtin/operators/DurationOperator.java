package com.misset.opp.callable.builtin.operators;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

public class DurationOperator extends BuiltInOperator {
    private DurationOperator() { }
    public static final DurationOperator INSTANCE = new DurationOperator();

    @Override
    public String getName() {
        return "DURATION";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.JSON_OBJECT;
    }
}
