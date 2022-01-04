package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.misset.opp.odt.psi.ODTPsiElement;
import com.misset.opp.resolvable.psi.PsiResolvable;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public interface ODTResolvable extends PsiResolvable, ODTPsiElement {

    default Set<OntResource> filter(Set<OntResource> resources) {
        return resources;
    }

    Set<OntResource> resolvePreviousStep();

    void inspect(ProblemsHolder holder);

    void annotate(AnnotationHolder holder);

}
