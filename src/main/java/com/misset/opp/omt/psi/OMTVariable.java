package com.misset.opp.omt.psi;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.psi.PsiVariable;
import com.misset.opp.omt.psi.impl.delegate.OMTVariableDelegate;
import org.jetbrains.yaml.psi.YAMLScalar;

public interface OMTVariable extends YAMLScalar, PsiVariable {
    Key<OMTVariableDelegate> WRAPPER = new Key<>("OMTVariableImplWrapper");

    /**
     * The name of the variable (without the $ prefix)
     */
    String getName();

    PsiElement setName(String newName);
}
