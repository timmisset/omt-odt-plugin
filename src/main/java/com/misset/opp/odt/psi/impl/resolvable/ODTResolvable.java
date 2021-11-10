package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Resolvable;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

/**
 * ODTResolvable are resolvable PsiElements in the ODT Language
 * For example, a DEFINE statement, any QueryStep / Query
 */
public interface ODTResolvable extends Resolvable, PsiElement {

    default Set<OntResource> filter(Set<OntResource> resources) {
        return resources;
    }

}
