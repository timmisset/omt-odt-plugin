package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import org.apache.jena.ontology.OntResource;

public class JsonOperator extends AbstractBuiltInOperator {
    private JsonOperator() {
    }

    public static final JsonOperator INSTANCE = new JsonOperator();

    @Override
    public String getName() {
        return "JSON";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        // The JSON operator is only ever used in the CAST operator
        // it should cast to an JSON_OBJECT
        return OntologyModelConstants.getJsonObject();
    }
}
