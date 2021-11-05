package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.psi.PsiElement;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public interface ODTResolvable extends PsiElement {

    Set<OntResource> resolve();

}
