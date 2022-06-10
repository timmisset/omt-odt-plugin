package com.misset.opp.odt.builtin.operators;

import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntResource;

public class JsonOperator extends BuiltInOperator {
    private JsonOperator() { }
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
        return OppModel.INSTANCE.JSON_OBJECT;
    }
}
