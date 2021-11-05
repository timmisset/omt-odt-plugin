package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.ODTVariable;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public interface ODTVariableDelegate {
    boolean isDeclaredVariable();
    boolean canBeDeclaredVariable(ODTVariable variable);
    PsiReference getReference();
    Set<OntResource> getType();
    boolean isOMTVariableProvider();
}
