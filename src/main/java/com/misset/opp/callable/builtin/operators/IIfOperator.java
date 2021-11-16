package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public class IIfOperator extends BuiltInOperator {
    private IIfOperator() {
    }

    public static final IIfOperator INSTANCE = new IIfOperator();

    @Override
    public String getName() {
        return "IIF";
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public Set<OntResource> resolveFrom(Set<OntResource> resources,
                                        Call call) {
        final HashSet<OntResource> output = new HashSet<>();
        output.addAll(call.resolveSignatureArgument(1));
        output.addAll(call.resolveSignatureArgument(2));
        return output;
    }
}
