package com.misset.opp.odt.builtin.operators;

import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class MapOperator extends AbstractBuiltInOperator {
    private MapOperator() {
    }

    public static final MapOperator INSTANCE = new MapOperator();
    private static final List<String> PARAMETER_NAMES = List.of("to");

    @Override
    public String getName() {
        return "MAP";
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           PsiCall call) {
        return call.resolveSignatureArgument(0);
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
