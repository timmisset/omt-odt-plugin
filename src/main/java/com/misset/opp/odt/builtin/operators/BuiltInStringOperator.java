package com.misset.opp.odt.builtin.operators;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

public abstract class BuiltInStringOperator extends AbstractBuiltInOperator {

    @Override
    public OntResource resolveSingle() {
        return OppModelConstants.XSD_STRING_INSTANCE;
    }
}
