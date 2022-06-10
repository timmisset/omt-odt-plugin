package com.misset.opp.odt.builtin.operators;

import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntResource;

public abstract class BuiltInStringOperator extends BuiltInOperator {

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.XSD_STRING_INSTANCE;
    }
}
