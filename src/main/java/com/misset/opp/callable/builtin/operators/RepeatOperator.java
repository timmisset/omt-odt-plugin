package com.misset.opp.callable.builtin.operators;

import com.misset.opp.odt.psi.impl.call.ODTCall;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public class RepeatOperator extends BuiltInOperator {
    private RepeatOperator() { }
    public static final RepeatOperator INSTANCE = new RepeatOperator();

    @Override
    public String getName() {
        return "REPEAT";
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    /**
     * REPEAT operator returns the type of both the input and the resolved outcome of the first argument
     */
    @Override
    public Set<OntResource> resolveFrom(Set<OntResource> resources,
                                        ODTCall call) {
        final HashSet<OntResource> ontResources = new HashSet<>(resources);
        ontResources.addAll(call.resolveSignatureArgument(0));
        return ontResources;
    }
}
