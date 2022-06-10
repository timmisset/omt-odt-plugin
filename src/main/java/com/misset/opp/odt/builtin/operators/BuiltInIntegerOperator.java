package com.misset.opp.odt.builtin.operators;

import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntResource;

public abstract class BuiltInIntegerOperator extends BuiltInOperator {

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.XSD_INTEGER_INSTANCE;
    }

    @Override
    protected boolean hasFixedReturnType() {
        return true;
    }
}
