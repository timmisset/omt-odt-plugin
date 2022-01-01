package com.misset.opp.odt;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.misset.opp.shared.refactoring.SupportsRefactoring;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;

public class ODTRefactoringSupport extends RefactoringSupportProvider {

    @Override
    public boolean isAvailable(@NotNull PsiElement context) {
        return context instanceof SupportsRefactoring;
    }

    @Override
    public boolean isSafeDeleteAvailable(@NotNull PsiElement element) {
        return element instanceof SupportsSafeDelete && ((SupportsSafeDelete) element).isUnused();
    }
}
