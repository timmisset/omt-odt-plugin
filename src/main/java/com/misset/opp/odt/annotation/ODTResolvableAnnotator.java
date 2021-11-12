package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.jetbrains.annotations.NotNull;

/**
 * Annotate of all elements that implement the ODTResolvable interface
 */
public class ODTResolvableAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        if(element instanceof ODTResolvable) {
            ((ODTResolvable)element).annotate(holder);
        }
    }
}
