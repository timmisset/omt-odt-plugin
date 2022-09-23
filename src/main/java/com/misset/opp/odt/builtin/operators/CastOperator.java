package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CastOperator extends AbstractBuiltInOperator {
    private CastOperator() {
    }

    public static final CastOperator INSTANCE = new CastOperator();
    private static final List<String> PARAMETER_NAMES = List.of("toType");

    @Override
    public String getName() {
        return "CAST";
    }

    /**
     * The CAST operator will cast the given set of Resources and cast it to the type specified in argument0
     * It uses the xsd ontology or specifically to an IRI (OWL_THING) or JSON_OBJECT
     */
    @Override
    public Set<OntResource> resolveFrom(Set<OntResource> resources,
                                        PsiCall call) {
        Set<OntResource> argument = call.resolveSignatureArgument(0);
        if (argument.contains(OntologyModelConstants.getIri())) {
            return Set.of(OntologyModelConstants.getOwlThingInstance());
        } else if (argument.contains(OntologyModelConstants.getXsdBoolean())) {
            return Set.of(OntologyModelConstants.getXsdBooleanInstance());
        }
        return OntologyModel.getInstance().toIndividuals(argument)
                .stream()
                .map(OntResource.class::cast)
                .collect(Collectors.toSet());
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
