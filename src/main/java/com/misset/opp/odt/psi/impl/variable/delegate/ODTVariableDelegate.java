package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiReference;
import com.misset.opp.callable.Variable;
import com.misset.opp.odt.psi.ODTVariable;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public interface ODTVariableDelegate {
    boolean isDeclaredVariable();

    boolean canBeDeclaredVariable(ODTVariable variable);

    boolean isAssignedVariable();

    PsiReference getReference();

    Set<OntResource> getType();

    Variable getDeclared();
}
