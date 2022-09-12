package com.misset.opp.odt.psi.resolvable;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.documentation.ODTDocumented;
import com.misset.opp.resolvable.psi.PsiResolvable;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public interface ODTResolvable extends PsiResolvable, ODTDocumented {

    default Set<OntResource> filter(Set<OntResource> resources) {
        return resources;
    }

    Set<OntResource> resolvePreviousStep();

    void inspect(ProblemsHolder holder);
}
