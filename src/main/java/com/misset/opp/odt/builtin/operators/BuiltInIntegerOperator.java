package com.misset.opp.odt.builtin.operators;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

public abstract class BuiltInIntegerOperator extends AbstractBuiltInOperator {

    @Override
    public OntResource resolveSingle() {
        return OppModelConstants.getXsdIntegerInstance();
    }

    @Override
    protected boolean hasFixedReturnType() {
        return true;
    }
}
