package com.misset.opp.omt.refactor;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OMTRefactoringSupport extends RefactoringSupportProvider {

    @Override
    public boolean isAvailable(@NotNull PsiElement context) {
        return super.isAvailable(context);
    }

    @Override
    public boolean isInplaceRenameAvailable(@NotNull PsiElement element,
                                            PsiElement context) {
        return super.isInplaceRenameAvailable(element, context);
    }

    @Override
    public boolean isMemberInplaceRenameAvailable(@NotNull PsiElement element,
                                                  @Nullable PsiElement context) {
        return super.isMemberInplaceRenameAvailable(element, context);
    }
}
