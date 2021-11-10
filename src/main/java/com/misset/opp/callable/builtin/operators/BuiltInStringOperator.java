package com.misset.opp.callable.builtin.operators;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

public abstract class BuiltInStringOperator extends BuiltInOperator {

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.XSD_STRING_INSTANCE;
    }
}
