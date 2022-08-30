package com.misset.opp.odt.builtin.operators;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

/**
 * ELEMENTS turns an OrderedList or JSON Array into a regular RDF array
 */
public class ElementsOperator extends AbstractBuiltInOperator {
    private ElementsOperator() {
    }

    public static final ElementsOperator INSTANCE = new ElementsOperator();

    @Override
    public String getName() {
        return "ELEMENTS";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModelConstants.OWL_THING_INSTANCE;
    }
}
