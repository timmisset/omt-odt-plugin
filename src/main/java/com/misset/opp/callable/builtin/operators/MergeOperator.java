package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public class MergeOperator extends BuiltInOperator {
    private MergeOperator() { }
    public static final MergeOperator INSTANCE = new MergeOperator();

    @Override
    public String getName() {
        return "MERGE";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           Call call) {
        final int numberOfArguments = call.numberOfArguments();
        final HashSet<OntResource> output = new HashSet<>();
        if (numberOfArguments == 1) {
            // combines the input + argument:
            output.addAll(resources);
            output.addAll(call.resolveSignatureArgument(0));
        } else if (numberOfArguments > 1) {
            // combines the arguments only
            for (int i = 0; i < numberOfArguments; i++) {
                output.addAll(call.resolveSignatureArgument(i));
            }
        }
        return output;
    }
}
