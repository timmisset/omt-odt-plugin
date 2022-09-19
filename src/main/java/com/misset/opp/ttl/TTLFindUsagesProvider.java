package com.misset.opp.ttl;

import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.ttl.psi.*;
import com.misset.opp.ttl.psi.extend.TTLQualifiedIriResolver;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class TTLFindUsagesProvider implements FindUsagesProvider {
    public static final Predicate<PsiElement> CHECK_CAN_FIND_USAGES = element ->
            isDeclarePrefix(element) || isSubjectIri(element) || isPredicateIri(element);

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return CHECK_CAN_FIND_USAGES.test(psiElement);
    }

    private static boolean isSubjectIri(PsiElement element) {
        TTLIri iri = PsiTreeUtil.getParentOfType(element, TTLIri.class, false);
        return iri != null && iri.getParent() instanceof TTLSubject;
    }

    private static boolean isPredicateIri(PsiElement element) {
        TTLIri iri = PsiTreeUtil.getParentOfType(element, TTLIri.class, false);
        return iri != null && iri.getParent() instanceof TTLObject && ((TTLObject) iri.getParent()).isPredicate();
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
        if (isPredicateIri(element)) {
            return "predicate";
        } else if (isSubjectIri(element)) {
            return "subject";
        } else if (isDeclarePrefix(element)) {
            return "prefix";
        }
        return "unknown type";
    }

    @Override
    public @Nls @NotNull String getDescriptiveName(@NotNull PsiElement element) {
        PsiElement parent = element.getParent();
        if (parent instanceof TTLIri) {
            return Optional.ofNullable(((TTLIri) parent).getQualifiedIri()).orElse("");
        } else if (parent instanceof TTLDeclarePrefix) {
            return parent.getText();
        }
        return element.getText();
    }

    @Override
    public @Nls @NotNull String getNodeText(@NotNull PsiElement element,
                                            boolean useFullName) {
        if (element instanceof TTLQualifiedIriResolver) {
            return Optional.ofNullable(((TTLQualifiedIriResolver) element).getQualifiedIri()).orElse(element.getText());
        }
        return element.getText();
    }
}
