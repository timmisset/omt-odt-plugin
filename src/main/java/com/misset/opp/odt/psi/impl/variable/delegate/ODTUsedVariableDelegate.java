package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.reference.ODTVariableReference;

public class ODTUsedVariableDelegate extends ODTBaseVariableDelegate  {

    public ODTUsedVariableDelegate(ODTVariable element) {
        super(element);
    }

    @Override
    public boolean isDeclaredVariable() {
        return !isAssignmentPart() && isOMTVariableProvider();
    }

    @Override
    public PsiReference getReference() {
        if(isDeclaredVariable()) { return null; }
        return new ODTVariableReference(element);
    }
}
