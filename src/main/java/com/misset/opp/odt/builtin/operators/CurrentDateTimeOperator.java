package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import org.apache.jena.ontology.OntResource;

public class CurrentDateTimeOperator extends AbstractBuiltInOperator {
    private CurrentDateTimeOperator() {
    }

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
        return OntologyModelConstants.getXsdDatetimeInstance();
    }

    @Override
    public boolean requiresInput() {
        return false;
    }
}
