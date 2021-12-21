package com.misset.opp.odt.builtin.operators;

import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class MapOperator extends BuiltInOperator {
    private MapOperator() { }
    public static final MapOperator INSTANCE = new MapOperator();

    @Override
    public String getName() {
        return "MAP";
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           PsiCall call) {
        return call.resolveSignatureArgument(0);
    }
}
