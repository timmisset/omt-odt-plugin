package com.misset.opp.callable.psi;

import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Resolvable;

/**
 * Any PsiElement that is resolvable should use this interface to indicate that it's also a valid PsiElement
 */
public interface PsiResolvable extends Resolvable, PsiElement {

    /**
     * If the resolvable PSI element is a reference to another resolvable or, for example, is a query.
     */
    default boolean isReference() {
        return false;
    }

}
