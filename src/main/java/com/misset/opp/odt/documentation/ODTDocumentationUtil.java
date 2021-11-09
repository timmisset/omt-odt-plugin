package com.misset.opp.odt.documentation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ODTDocumentationUtil {
    private static final List<Class<? extends PsiElement>> validDocOwners = List.of(
            ODTDefineStatement.class
    );

    @Nullable
    public static String getJavaDocComment(PsiElement element) {
        final PsiElement docEnd = PsiTreeUtil.prevVisibleLeaf(element);
        if (docEnd == null || !(docEnd.getParent() instanceof PsiDocComment)) {
            return null;
        }
        return Arrays.stream(((PsiDocComment) docEnd.getParent()).getDescriptionElements())
                .map(PsiElement::getText)
                .collect(Collectors.joining("<br>"));
    }

    @Nullable
    public static PsiElement getDocOwner(PsiDocTag docTag) {
        PsiElement psiElement = PsiTreeUtil.nextVisibleLeaf(docTag.getContainingComment());
        while (psiElement != null && !isValidDocOwner(psiElement)) {
            psiElement = psiElement.getParent();
        }
        return psiElement;
    }

    private static boolean isValidDocOwner(PsiElement element) {
        return element != null && validDocOwners.stream()
                .anyMatch(docOwnerClass -> docOwnerClass.isAssignableFrom(element.getClass()));
    }

}
