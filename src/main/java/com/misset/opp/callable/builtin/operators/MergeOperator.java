package com.misset.opp.callable.builtin.operators;

import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.impl.call.ODTCall;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.List;
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
                                           ODTCall call) {
        final List<ODTSignatureArgument> signatureArguments = call.getSignatureArguments();
        final HashSet<OntResource> output = new HashSet<>();
        if(signatureArguments.size() == 1) {
            // combines the input + argument:
             output.addAll(resources);
             output.addAll(call.resolveSignatureArgument(0));
        } else if(signatureArguments.size() > 1) {
            // combines the arguments only
            for(int i = 0; i < signatureArguments.size(); i++) {
                output.addAll(call.resolveSignatureArgument(i));
            }
        }
        return output;
    }
}
