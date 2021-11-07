package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Generic annotator for context-less annotations
 * Implement ODTAnnotatedElement on the ODT PsiElements that should be annotated
 */
public class ODTAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        if(element instanceof ODTAnnotatedElement) {
            ((ODTAnnotatedElement)element).annotate(holder);
        }
    }
}
