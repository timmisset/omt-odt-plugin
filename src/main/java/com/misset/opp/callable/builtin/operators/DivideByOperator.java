package com.misset.opp.callable.builtin.operators;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

public class DivideByOperator extends BuiltInOperator {
    private DivideByOperator() { }
    public static final DivideByOperator INSTANCE = new DivideByOperator();

    @Override
    public String getName() {
        return "DIVIDE_BY";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.XSD_DECIMAL_INSTANCE;
    }
}
