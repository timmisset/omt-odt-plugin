package com.misset.opp.callable.builtin.operators;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class TypeOperator extends BuiltInOperator {
    private TypeOperator() { }
    public static final TypeOperator INSTANCE = new TypeOperator();

    @Override
    public String getName() {
        return "TYPE";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources) {
        return OppModel.INSTANCE.toClasses(resources);
    }
}
