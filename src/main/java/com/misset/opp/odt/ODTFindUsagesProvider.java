package com.misset.opp.odt;

import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.odt.psi.ODTVariable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTFindUsagesProvider implements FindUsagesProvider {
    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof ODTDefineName ||
                isDeclaredVariable(psiElement);
    }

    private boolean isDeclaredVariable(PsiElement psiElement) {
        if(!(psiElement instanceof ODTVariable)) { return false; }
        final ODTVariable variable = (ODTVariable) psiElement;
        return variable.isDeclaredVariable();
    }

    @Override
    public @Nullable @NonNls String getHelpId(@NotNull PsiElement psiElement) {
        return "null";
    }

    @Override
    public @Nls @NotNull String getType(@NotNull PsiElement element) {
        return "null";
    }

    @Override
    public @Nls @NotNull String getDescriptiveName(@NotNull PsiElement element) {
        return "null";
    }

    @Override
    public @Nls @NotNull String getNodeText(@NotNull PsiElement element,
                                            boolean useFullName) {
        return "null";
    }
}
