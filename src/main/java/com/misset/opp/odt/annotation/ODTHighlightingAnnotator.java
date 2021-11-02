package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.odt.syntax.ODTSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

public class ODTHighlightingAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        if(element instanceof ODTCallName) {
            setHighlightingAnnotation(holder, ODTSyntaxHighlighter.BaseCallAttributesKey);
        } else if (element instanceof ODTDefineName) {
            setHighlightingAnnotation(holder, ODTSyntaxHighlighter.DefineAttributesKey);
        }
    }
    private void setHighlightingAnnotation(AnnotationHolder holder, TextAttributesKey textAttributesKey) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .textAttributes(textAttributesKey)
                .create();
    }
}
