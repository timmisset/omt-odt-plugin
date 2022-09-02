package com.misset.opp.odt.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class ODTASTWrapperPsiElement extends ASTWrapperPsiElement implements PsiElement {
    public ODTASTWrapperPsiElement(@NotNull ASTNode node) {
        super(node);
    }

}
