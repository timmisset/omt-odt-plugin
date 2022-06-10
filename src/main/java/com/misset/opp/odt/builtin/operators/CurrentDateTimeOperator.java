package com.misset.opp.odt.builtin.operators;

import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntResource;

public class CurrentDateTimeOperator extends BuiltInOperator {
    private CurrentDateTimeOperator() { }
    public static final CurrentDateTimeOperator INSTANCE = new CurrentDateTimeOperator();

    @Override
    public String getName() {
        return "CURRENT_DATETIME";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.XSD_DATETIME_INSTANCE;
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
