package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.ODTVariable;

public class ODTDeclaredVariableDelegate extends ODTBaseVariableDelegate {

    public ODTDeclaredVariableDelegate(ODTVariable element) {
        super(element);
    }

    @Override
    public boolean isDeclaredVariable() {
        return true;
    }

    @Override
    public PsiReference getReference() {
        return null;
    }
}
