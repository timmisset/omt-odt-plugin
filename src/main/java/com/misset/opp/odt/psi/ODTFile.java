package com.misset.opp.odt.psi;

import com.intellij.psi.PsiFile;
import org.jetbrains.yaml.psi.YAMLPsiElement;

public interface ODTFile extends PsiFile {

    YAMLPsiElement getHost();
}
