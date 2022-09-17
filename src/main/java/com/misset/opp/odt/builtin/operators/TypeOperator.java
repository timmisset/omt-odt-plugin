package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyResourceUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;

import java.util.Set;
import java.util.stream.Collectors;

public class TypeOperator extends AbstractBuiltInOperator {
    private TypeOperator() {
    }

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
        OntClass ontClass = OntologyModel.getInstance().toClass(resource);
        if (OntologyResourceUtil.isType(ontClass)) {
            return ontClass;
        } else {
            return OntologyModelConstants.getIri();
        }
    }
}
