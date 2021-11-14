package com.misset.opp.odt.psi;

import com.intellij.psi.PsiElement;

public interface ODTPsiElement extends PsiElement {

    ODTFile getContainingFile();

}
