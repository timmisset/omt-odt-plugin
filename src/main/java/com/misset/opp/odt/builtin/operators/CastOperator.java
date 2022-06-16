package com.misset.opp.odt.builtin.operators;

import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

import java.util.Set;
import java.util.stream.Collectors;

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
                                        PsiCall call) {
        Set<OntResource> argument = call.resolveSignatureArgument(0);
        if (argument.contains(OppModelConstants.IRI)) {
            return Set.of(OppModelConstants.OWL_THING_INSTANCE);
        }
        return OppModel.INSTANCE.toIndividuals(argument)
                .stream()
                .map(OntResource.class::cast)
                .collect(Collectors.toSet());
    }
}
