package com.misset.opp.odt.refactoring;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.intellij.refactoring.RefactoringActionHandler;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.shared.refactoring.SupportsRefactoring;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTRefactoringSupport extends RefactoringSupportProvider {

    @Override
    public boolean isAvailable(@NotNull PsiElement context) {
        return context instanceof SupportsRefactoring;
    }

    @Override
    public boolean isSafeDeleteAvailable(@NotNull PsiElement element) {
        return element instanceof SupportsSafeDelete && ((SupportsSafeDelete) element).isUnused();
    }

    @Override
    public boolean isInplaceRenameAvailable(@NotNull PsiElement element, PsiElement context) {
        return element instanceof ODTVariable;
    }

    @Override
    public boolean isInplaceIntroduceAvailable(@NotNull PsiElement element, PsiElement context) {
        return super.isInplaceIntroduceAvailable(element, context);
    }

    @Override
    public @Nullable RefactoringActionHandler getIntroduceVariableHandler(PsiElement element) {
        return super.getIntroduceVariableHandler(element);
    }
}