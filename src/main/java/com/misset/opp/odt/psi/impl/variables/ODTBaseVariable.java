package com.misset.opp.odt.psi.impl.variables;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.ODTVariableImpl;
import org.jetbrains.annotations.NotNull;

public abstract class ODTBaseVariable extends ODTVariableImpl implements ODTVariable, PsiNamedElement {
    public ODTBaseVariable(@NotNull ASTNode node) {
        super(node);
    }

    abstract public boolean isDefinedVariable();

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return this;
    }

    @Override
    public String getName() {
        return getText();
    }
}
