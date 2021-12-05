package com.misset.opp.callable.builtin.operators;

import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.ResourceUtil;
import com.misset.opp.ttl.util.TTLValidationUtil;
import com.misset.opp.ttl.util.TTLValueParserUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;
import java.util.stream.Collectors;

public class TypeOperator extends BuiltInOperator {
    private TypeOperator() { }
    public static final TypeOperator INSTANCE = new TypeOperator();

    @Override
    public String getName() {
        return "TYPE";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources) {
        return resources.stream().map(this::mapToType).collect(Collectors.toSet());
    }
    private OntResource mapToType(OntResource resource) {
        if(ResourceUtil.isType(resource)) {
            return resource.asIndividual().getOntClass();
        } else {
            return OppModel.INSTANCE.IRI;
        }
    }
}
