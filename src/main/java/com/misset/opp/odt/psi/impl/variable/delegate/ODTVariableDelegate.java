package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.resolvable.Variable;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public interface ODTVariableDelegate {
    boolean isDeclaredVariable();

    boolean canBeDeclaredVariable(ODTVariable variable);

    boolean isAssignedVariable();

    PsiReference getReference();

    Set<OntResource> resolve();

    Variable getDeclared();
}
