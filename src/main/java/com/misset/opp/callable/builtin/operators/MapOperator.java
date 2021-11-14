package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class MapOperator extends BuiltInOperator {
    private MapOperator() { }
    public static final MapOperator INSTANCE = new MapOperator();

    @Override
    public String getName() {
        return "MAP";
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           Call call) {
        return call.resolveSignatureArgument(0);
    }
}
