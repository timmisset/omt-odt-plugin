package com.misset.opp.odt.psi.wrapping.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.references.ODTVariableReference;
import com.misset.opp.odt.psi.wrapping.ODTVariableWrapper;
import com.misset.opp.omt.psi.references.OMTVariableReference;
import org.jetbrains.annotations.NotNull;

public class ODTUsageVariableImpl extends ODTBaseVariable implements ODTVariableWrapper {
    public ODTUsageVariableImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isDefinedVariable() {
        return false;
    }

    @Override
    public PsiReference getReference() {
        return new ODTVariableReference(this);
    }
}
