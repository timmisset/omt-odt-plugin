package com.misset.opp.callable.builtin.operators;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

public class GraphOperator extends BuiltInOperator {
    private GraphOperator() { }
    public static final GraphOperator INSTANCE = new GraphOperator();

    @Override
    public String getName() {
        return "GRAPH";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.NAMED_GRAPH;
    }
}
