package com.misset.opp.callable.builtin.operators;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

public class IriOperator extends BuiltInOperator {
    private IriOperator() { }
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
        // The IRI operator is only ever used in the CAST operator
        // it should cast to an OWL_THING
        return OppModel.INSTANCE.OWL_THING;
    }
}
