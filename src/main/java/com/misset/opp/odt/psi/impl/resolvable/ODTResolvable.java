package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Resolvable;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.Set;

/**
 * ODTResolvable are resolvable PsiElements in the ODT Language
 * For example, a DEFINE statement, any QueryStep / Query
 */
public interface ODTResolvable extends Resolvable, PsiElement {

    default Set<OntResource> filter(Set<OntResource> resources) {
        return resources;
    }

    default Set<OntResource> resolvePreviousStep() { return Collections.emptySet(); }

    void inspect(ProblemsHolder holder);

    void annotate(AnnotationHolder holder);

}
