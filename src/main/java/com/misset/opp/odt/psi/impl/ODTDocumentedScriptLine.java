package com.misset.opp.odt.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ODTDocumentedScriptLine extends ASTWrapperPsiElement implements PsiJavaDocumentedElement {
    protected ODTDocumentedScriptLine(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @Nullable PsiDocComment getDocComment() {
        PsiElement firstChild = getFirstChild();
        return firstChild instanceof PsiDocComment ? (PsiDocComment) firstChild : null;
    }
}
