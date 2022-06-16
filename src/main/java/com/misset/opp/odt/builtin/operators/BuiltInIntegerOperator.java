package com.misset.opp.odt.builtin.operators;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

public abstract class BuiltInIntegerOperator extends BuiltInOperator {

    @Override
    public OntResource resolveSingle() {
        return OppModelConstants.XSD_INTEGER_INSTANCE;
    }

    @Override
    protected boolean hasFixedReturnType() {
        return true;
    }
}
