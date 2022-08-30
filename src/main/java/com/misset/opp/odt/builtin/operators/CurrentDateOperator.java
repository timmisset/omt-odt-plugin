package com.misset.opp.odt.builtin.operators;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

public class CurrentDateOperator extends AbstractBuiltInOperator {
    private CurrentDateOperator() {
    }

    public static final CurrentDateOperator INSTANCE = new CurrentDateOperator();

    @Override
    public String getName() {
        return "CURRENT_DATE";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModelConstants.XSD_DATE_INSTANCE;
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
