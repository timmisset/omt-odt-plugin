package com.misset.opp.callable.builtin.operators;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

public class FloorOperator extends BuiltInOperator {
    private FloorOperator() { }
    public static final FloorOperator INSTANCE = new FloorOperator();

    @Override
    public String getName() {
        return "FLOOR";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.XSD_INTEGER_INSTANCE;
    }
}
