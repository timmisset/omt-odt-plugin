package com.misset.opp.ttl;

import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.ttl.psi.*;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class TTLFindUsagesProvider implements FindUsagesProvider {
    public static final Predicate<PsiElement> CHECK_CAN_FIND_USAGES = element ->
            isDeclarePrefix(element) || isSubjectIri(element);

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return CHECK_CAN_FIND_USAGES.test(psiElement);
    }

    private static boolean isSubjectIri(PsiElement element) {
        TTLIri iri = PsiTreeUtil.getParentOfType(element, TTLIri.class, false);
        return iri != null && iri.getParent() instanceof TTLSubject;
    }

    private static boolean isDeclarePrefix(PsiElement element) {
        return element instanceof TTLPrefix &&
                element.getParent() instanceof TTLDeclarePrefix;
    }

    @Override
    public @Nullable @NonNls String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @Override
    public @Nls @NotNull String getType(@NotNull PsiElement element) {
        if (element instanceof TTLPrefix) {
            return "prefix";
        } else if (element instanceof TTLSubject) {
            return "subject";
        } else if (element instanceof TTLPredicate) {
            return "predicate";
        } else if (element instanceof TTLStubBasedObject) {
            return "object";
        }
        return "unknown type";
    }

    @Override
    public @Nls @NotNull String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof TTLIri) {
            return Optional.ofNullable(((TTLIri) element).getQualifiedIri()).orElse(element.getText());
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
