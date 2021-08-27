package com.misset.opp.odt.psi.wrapping;

import com.intellij.psi.PsiNamedElement;
import com.misset.opp.odt.psi.ODTVariable;

/**
 * Wrapper for ODT variable generated PsiElement
 */
public interface ODTVariableWrapper extends ODTVariable, PsiNamedElement {

    boolean isDefinedVariable();

}
