package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import org.apache.jena.ontology.OntResource;

public abstract class BuiltInIntegerOperator extends AbstractBuiltInOperator {

    @Override
    public OntResource resolveSingle() {
        return OntologyModelConstants.getXsdIntegerInstance();
    }

    @Override
    protected boolean hasFixedReturnType() {
        return true;
    }
}
