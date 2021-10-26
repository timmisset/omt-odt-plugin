package com.misset.opp.odt.psi.impl.prefix;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class ODTBaseNamespacePrefix extends ASTWrapperPsiElement implements PsiNamedElement {
    public ODTBaseNamespacePrefix(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Override
    @NotNull
    public String getName() {
        return getFirstChild().getText();
    }
}
