package com.misset.opp.odt.builtin.operators;

import com.intellij.openapi.project.Project;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyResourceUtil;
import com.misset.opp.resolvable.psi.PsiCall;
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
    protected Set<OntResource> resolveFrom(Set<OntResource> resources, PsiCall call) {
        return resources.stream().map(resource -> mapToType(resource, call.getProject())).collect(Collectors.toSet());
    }

    private OntResource mapToType(OntResource resource, Project project) {
        OntClass ontClass = OntologyModel.getInstance(project).toClass(resource);
        if (OntologyResourceUtil.getInstance(project).isType(ontClass)) {
            return ontClass;
        } else {
            return OntologyModelConstants.getIri();
        }
    }
}
