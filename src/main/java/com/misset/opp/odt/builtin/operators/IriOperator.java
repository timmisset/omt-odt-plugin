package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import org.apache.jena.ontology.OntResource;

public class IriOperator extends AbstractBuiltInOperator {
    private IriOperator() {
    }

    public static final IriOperator INSTANCE = new IriOperator();

    @Override
    public String getName() {
        return "IRI";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        return OntologyModelConstants.getIri();
    }
}
