package com.misset.opp.callable.psi;

import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Callable;

/**
 * Any PsiElement that is callable should use this interface to indicate that it's also a valid PsiElement
 */
public interface PsiCallable extends Callable, PsiElement {

    PsiElement getCallTarget();

}
