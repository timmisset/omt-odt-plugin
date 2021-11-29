package com.misset.opp.omt.psi;

import com.intellij.openapi.util.Key;
import com.misset.opp.callable.psi.PsiVariable;
import com.misset.opp.omt.psi.impl.variable.OMTVariableImpl;
import org.jetbrains.yaml.psi.YAMLScalar;

public interface OMTVariable extends YAMLScalar, PsiVariable {
    Key<OMTVariableImpl> WRAPPER = new Key<>("OMTVariableImplWrapper");

    /**
     * The name of the variable (without the $ prefix)
     */
    String getName();

    void setName(String newName);
}
