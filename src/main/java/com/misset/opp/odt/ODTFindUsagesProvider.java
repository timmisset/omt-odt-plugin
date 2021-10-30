package com.misset.opp.odt;

import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTDefineCommandStatement;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.ODTDefineQueryStatement;
import com.misset.opp.odt.psi.ODTVariable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ODTFindUsagesProvider implements FindUsagesProvider {
    public static final Predicate<PsiElement> CHECK_CAN_FIND_USAGES = element -> element instanceof ODTDefineName ||
            isDeclaredVariable(element);

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return CHECK_CAN_FIND_USAGES.test(psiElement);
    }

    private static boolean isDeclaredVariable(PsiElement psiElement) {
        if (!(psiElement instanceof ODTVariable)) {
            return false;
        }
        final ODTVariable variable = (ODTVariable) psiElement;
        return variable.isDeclaredVariable();
    }

    @Override
    public @Nullable @NonNls String getHelpId(@NotNull PsiElement psiElement) {
        return "null";
    }

    @Override
    public @Nls @NotNull String getType(@NotNull PsiElement element) {
        if(element instanceof ODTVariable) {
            if(element.getParent() instanceof ODTDefineParam) {
                return "parameter";
            }
            return "variable";
        } else if(element instanceof ODTDefineQueryStatement) {
            return "query";
        } else if(element instanceof ODTDefineCommandStatement) {
            return "command";
        }
        return "null";
    }

    @Override
    public @Nls @NotNull String getDescriptiveName(@NotNull PsiElement element) {
        return element.getText();
    }

    @Override
    public @Nls @NotNull String getNodeText(@NotNull PsiElement element,
                                            boolean useFullName) {
        return element.getText();
    }
}
