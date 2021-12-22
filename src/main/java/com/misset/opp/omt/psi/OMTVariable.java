package com.misset.opp.omt.psi;

import com.intellij.psi.PsiElement;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.jetbrains.yaml.psi.YAMLScalar;

public interface OMTVariable extends YAMLScalar, PsiVariable {
    /**
     * The name of the variable (without the $ prefix)
     */
    String getName();

    PsiElement setName(String newName);
}
