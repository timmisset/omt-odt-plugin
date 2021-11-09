package com.misset.opp.odt.documentation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ODTDocumentationUtil {

    public static String getJavaDocComment(PsiElement element) {
        final PsiElement docEnd = PsiTreeUtil.prevVisibleLeaf(element);
        if(docEnd == null || !(docEnd.getParent() instanceof PsiDocComment)) {
            return null;
        }
        return Arrays.stream(((PsiDocComment) docEnd.getParent()).getDescriptionElements())
                .map(PsiElement::getText)
                .collect(Collectors.joining("<br>"));
    }

}
