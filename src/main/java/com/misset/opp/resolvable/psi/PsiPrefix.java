package com.misset.opp.resolvable.psi;

import com.intellij.psi.PsiElement;

public interface PsiPrefix extends PsiElement {

    String getNamespace();

    PsiElement getNamePsiElement();

    PsiElement getNamespacePsiElement();

    String getName();

}
