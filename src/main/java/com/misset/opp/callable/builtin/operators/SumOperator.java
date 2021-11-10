package com.misset.opp.callable.builtin.operators;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class SumOperator extends BuiltInOperator {
    private SumOperator() { }
    public static final SumOperator INSTANCE = new SumOperator();

    @Override
    public String getName() {
        return "SUM";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources) {
        return resources.stream().allMatch(OppModel.INSTANCE.XSD_INTEGER_INSTANCE::equals) ?
                resources :
                Set.of(OppModel.INSTANCE.XSD_DECIMAL_INSTANCE);
    }
}
