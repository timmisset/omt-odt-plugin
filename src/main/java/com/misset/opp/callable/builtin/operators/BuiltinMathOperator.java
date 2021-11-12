package com.misset.opp.callable.builtin.operators;

import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class BuiltinMathOperator extends BuiltInOperator {

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           ODTCall call) {
        final List<ODTSignatureArgument> signatureArguments = call.getSignatureArguments();
        Set<OntResource> values = new HashSet<>();
        if(signatureArguments.size() == 1) {
            // input = value, argument = subtraction
            values.addAll(resources);
            values.addAll(call.resolveSignatureArgument(0));
        } else if(signatureArguments.size() > 1) {
            values.addAll(call.resolveSignatureArgument(0));
            values.addAll(call.resolveSignatureArgument(1));
        }
        if(values.stream().allMatch(OppModel.INSTANCE.XSD_INTEGER_INSTANCE::equals)) {
            return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
        } else {
            return Set.of(OppModel.INSTANCE.XSD_DECIMAL_INSTANCE);
        }
    }

}
