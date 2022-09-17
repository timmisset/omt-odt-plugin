package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import org.apache.jena.ontology.OntResource;

public class BlankNodeOperator extends AbstractBuiltInOperator {
    private BlankNodeOperator() {
    }

    public static final BlankNodeOperator INSTANCE = new BlankNodeOperator();

    @Override
    public String getName() {
        return "BLANK_NODE";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        return OntologyModelConstants.getBlankNode();
    }
}
