package com.misset.opp.odt.psi.wrapping.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTDefineQueryStatement;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.impl.ODTVariableImpl;
import com.misset.opp.odt.psi.wrapping.ODTVariableWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ODTBaseVariable extends ODTVariableImpl implements ODTVariableWrapper {
    public ODTBaseVariable(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return this;
    }

    @Override
    public String getName() {
        return getText();
    }
}
