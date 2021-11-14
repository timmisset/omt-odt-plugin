package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public class CatchOperator extends BuiltInOperator {
    private CatchOperator() { }
    public static final CatchOperator INSTANCE = new CatchOperator();

    @Override
    public String getName() {
        return "CATCH";
    }

    /**
     * The CATCH operator will either return the input resources or, when an error occurs, the provided
     * value in argument0.
     * At compilation both types are possible
     */
    @Override
    public Set<OntResource> resolveFrom(Set<OntResource> resources,
                                        Call call) {
        final HashSet<OntResource> ontResources = new HashSet<>(resources);
        ontResources.addAll(call.resolveSignatureArgument(0));
        return ontResources;
    }

    @Override
    public Set<OntResource> resolveError(Set<OntResource> resources,
                                         Call call) {
        return call.resolveSignatureArgument(0);
    }
}
