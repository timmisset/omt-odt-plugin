package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import org.apache.jena.ontology.OntResource;

public abstract class BuiltInStringOperator extends AbstractBuiltInOperator {

    @Override
    public OntResource resolveSingle() {
        return OntologyModelConstants.getXsdStringInstance();
    }
}
