package com.misset.opp.resolvable.psi;

import com.intellij.psi.PsiElement;
import com.misset.opp.resolvable.Callable;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public interface PsiCallable extends Callable, PsiElement, PsiResolvable {

    Set<OntResource> getParamType(int index);

}
