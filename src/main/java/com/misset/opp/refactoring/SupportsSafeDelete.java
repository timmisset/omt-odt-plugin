package com.misset.opp.refactoring;

/**
 * Indicates that the PsiElement can be removed safely when Safe delete is clicked by the user
 */
public interface SupportsSafeDelete extends SupportsRefactoring {
    boolean isUnused();
}
