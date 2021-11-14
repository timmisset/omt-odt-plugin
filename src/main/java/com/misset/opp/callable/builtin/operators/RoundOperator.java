package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.Call;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

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
                                           Call call) {
        final int numberOfArguments = call.numberOfArguments();
        if (numberOfArguments == 0) {
            return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
        } else {
            if ("0".equals(call.getSignatureValue(0))) {
                return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
            }
            return Set.of(OppModel.INSTANCE.XSD_DECIMAL_INSTANCE);
        }
    }
}
