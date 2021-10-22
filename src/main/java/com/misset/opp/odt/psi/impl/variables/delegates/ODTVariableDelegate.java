package com.misset.opp.odt.psi.impl.variables.delegates;

import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.ODTVariable;

public interface ODTVariableDelegate {
    boolean isDeclaredVariable();
    boolean canBeDeclaredVariable(ODTVariable variable);
    PsiReference getReference();
}
