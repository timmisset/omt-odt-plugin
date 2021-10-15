package com.misset.opp.odt.psi.impl.variables;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.references.ODTVariableReference;
import org.jetbrains.annotations.NotNull;

public class ODTUsageVariableImpl extends ODTBaseVariable {
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
