package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.Call;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public abstract class BuiltinMathOperator extends BuiltInOperator {

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           Call call) {
        final int signatureArguments = call.numberOfArguments();
        Set<OntResource> values = new HashSet<>();
        if (signatureArguments == 1) {
            // input = value, argument = subtraction
            values.addAll(resources);
            values.addAll(call.resolveSignatureArgument(0));
        } else if (signatureArguments > 1) {
            values.addAll(call.resolveSignatureArgument(0));
            values.addAll(call.resolveSignatureArgument(1));
        }
        if (values.stream().allMatch(OppModel.INSTANCE.XSD_INTEGER_INSTANCE::equals)) {
            return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
        } else {
            return Set.of(OppModel.INSTANCE.XSD_DECIMAL_INSTANCE);
        }
    }

}
