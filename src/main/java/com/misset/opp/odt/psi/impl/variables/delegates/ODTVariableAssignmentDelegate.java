package com.misset.opp.odt.psi.impl.variables.delegates;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.ODTDeclareVariable;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.references.ODTVariableReference;

import java.util.Optional;

public class ODTVariableAssignmentDelegate extends ODTDeclaredVariableDelegate {

    public ODTVariableAssignmentDelegate(ODTVariable element) {
        super(element);
    }

    @Override
    public boolean isDeclaredVariable() {
        return Optional.ofNullable(element.getParent())
                .map(PsiElement::getParent)
                .map(ODTDeclareVariable.class::isInstance)
                .orElse(isOMTVariableProvider());
    }

    @Override
    public boolean canBeDeclaredVariable(ODTVariable variable) {
        return isDeclaredVariable() && super.canBeDeclaredVariable(variable);
    }

    @Override
    public PsiReference getReference() {
        return !isDeclaredVariable() ? new ODTVariableReference(element) : null;
    }
}
