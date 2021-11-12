package com.misset.opp.callable.builtin.operators;

import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class RoundOperator extends BuiltInOperator {
    private RoundOperator() { }
    public static final RoundOperator INSTANCE = new RoundOperator();

    @Override
    public String getName() {
        return "ROUND";
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           ODTCall call) {
        final List<ODTSignatureArgument> signatureArguments = call.getSignatureArguments();
        if(signatureArguments.isEmpty()) {
            return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
        } else {
            if(signatureArguments.get(0).getText().equals("0")) {
                return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
            }
            return Set.of(OppModel.INSTANCE.XSD_DECIMAL_INSTANCE);
        }
    }
}
