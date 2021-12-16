package com.misset.opp.ttl;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.misset.opp.ttl.psi.TTLSubject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TTLRefactoringSupportProvider extends RefactoringSupportProvider {

    @Override
    public boolean isMemberInplaceRenameAvailable(@NotNull PsiElement element, @Nullable PsiElement context) {
        return false;
    }

    @Override
    public boolean isAvailable(@NotNull PsiElement context) {
        return canBeRenamed(context);
    }

    private boolean canBeRenamed(PsiElement element) {
        return element instanceof TTLSubject;
    }
}
