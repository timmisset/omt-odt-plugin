package com.misset.opp.ttl;

import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.misset.opp.ttl.psi.TTLPrefixId;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.psi.prefix.TTLBasePrefixedName;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TTLFindUsagesProvider implements FindUsagesProvider {
    public static final Predicate<PsiElement> CHECK_CAN_FIND_USAGES = element ->
            element instanceof TTLPrefixId ||
                    element instanceof TTLBasePrefixedName || element instanceof TTLSubject;

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return CHECK_CAN_FIND_USAGES.test(psiElement);
    }

    @Override
    public @Nullable @NonNls String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @Override
    public @Nls @NotNull String getType(@NotNull PsiElement element) {
        if (element instanceof TTLPrefixId) {
            return "prefix";
        } else if (element instanceof TTLBasePrefixedName) {
            return "prefix";
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
        if (element instanceof PsiNamedElement) {
            @Nullable @NlsSafe String name = ((PsiNamedElement) element).getName();
            if (name != null) {
                return name;
            }
        }
        return element.getText();
    }
}
