package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.ODTSyntaxHighlighter;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.resolvable.Variable;
import org.jetbrains.annotations.NotNull;

/**
 * Highlighting for the resolved types is set by the*
 */
public class ODTHighlightingAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        if (element instanceof ODTVariable) {
            ODTVariable variable = (ODTVariable) element;
            Variable declared = variable.getDeclared();
            if (declared == null) {
                return;
            }
            if (declared.isReadonly()) {
                setHighlightingAnnotation(holder, ODTSyntaxHighlighter.ReadonlyVariable);
            }
            if (declared.isGlobal()) {
                setHighlightingAnnotation(holder, ODTSyntaxHighlighter.GlobalVariable);
            }
            if (declared.isParameter()) {
                setHighlightingAnnotation(holder, ODTSyntaxHighlighter.Parameter);
            }
        }
    }

    private void setHighlightingAnnotation(AnnotationHolder holder, TextAttributesKey textAttributesKey) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .textAttributes(textAttributesKey)
                .create();
    }
}
