package com.misset.opp.callable;

import com.intellij.psi.PsiElement;

/**
 * Any PsiElement that is resolvable should use this interface to indicate that it's also a valid PsiElement
 */
public interface PsiResolvable extends Resolvable, PsiElement {

}
