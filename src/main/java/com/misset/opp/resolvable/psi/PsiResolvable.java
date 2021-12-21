package com.misset.opp.resolvable.psi;

import com.intellij.psi.PsiElement;
import com.misset.opp.resolvable.ContextResolvable;


public interface PsiResolvable extends ContextResolvable, PsiElement {

    /**
     * If the resolvable PSI element is a reference to another resolvable or, for example, is a query.
     */
    default boolean isReference() {
        return false;
    }

}
