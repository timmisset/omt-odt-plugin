package com.misset.opp.odt.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTPsiElement;
import org.jetbrains.annotations.NotNull;

public class ODTASTWrapperPsiElement extends ASTWrapperPsiElement implements ODTPsiElement {
    public ODTASTWrapperPsiElement(@NotNull ASTNode node) {
        super(node);
    }

    public ODTFile getODTFile() {
        return (ODTFile) super.getContainingFile();
    }
}
