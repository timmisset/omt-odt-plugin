package com.misset.opp.callable.builtin.operators;

import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class CastOperator extends BuiltInOperator {
    private CastOperator() {
    }

    public static final CastOperator INSTANCE = new CastOperator();

    @Override
    public String getName() {
        return "CAST";
    }

    @Override
    /**
     * The CAST operator will cast the given set of Resources and cast it to the type specified in argument0
     * It uses the xsd ontology or specifically to an IRI (OWL_THING) or JSON_OBJECT
     */
    public Set<OntResource> resolveFrom(Set<OntResource> resources,
                                    ODTCall call) {
       return OppModel.INSTANCE.toIndividuals(call.resolveSignatureArgument(0));
    }
}
