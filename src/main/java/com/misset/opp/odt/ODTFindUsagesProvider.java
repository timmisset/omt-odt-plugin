package com.misset.opp.odt;

import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class ODTFindUsagesProvider implements FindUsagesProvider {
    public static final Predicate<PsiElement> CHECK_CAN_FIND_USAGES = element ->
            element instanceof PsiCallable ||
                    isDeclaredVariable(element);

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return CHECK_CAN_FIND_USAGES.test(psiElement);
    }

    private static boolean isDeclaredVariable(PsiElement psiElement) {
        if (!(psiElement instanceof ODTVariable)) {
            return false;
        }
        return ((ODTVariable) psiElement).isDeclaredVariable();
    }

    @Override
    public @Nullable @NonNls String getHelpId(@NotNull PsiElement psiElement) {
        return null;
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
        } else if (element instanceof ODTDefineCommandStatement) {
            return "command";
        } else if (element instanceof ODTOperatorCall) {
            return "operator";
        } else if (element instanceof ODTCommandCall) {
            return "command";
        }
        return "null";
    }

    @Override
    public @Nls @NotNull String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof ODTCall) {
            // unresolved call but might be a local or built-in:
            ODTCall call = (ODTCall) element;
            Callable callable = (call).getCallable();
            return callable != null ? callable.getDescription(call.getLocalCommandProvider(), element.getProject()) : element.getText();
        } else if (element instanceof PsiCallable) {
            return ((PsiCallable) element).getName();
        } else if (element instanceof PsiVariable) {
            return ((PsiVariable) element).getName();
        }
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
