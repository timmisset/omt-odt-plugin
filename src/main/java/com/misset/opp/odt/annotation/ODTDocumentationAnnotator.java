package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.documentation.ODTDocumented;
import org.jetbrains.annotations.NotNull;

public class ODTDocumentationAnnotator implements Annotator {


    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof ODTDocumented) {
            String documentation = ((ODTDocumented) element).getDocumentation();
            if (documentation != null) {
                holder.newAnnotation(HighlightSeverity.INFORMATION, "").tooltip(documentation).create();
            }
        }
    }
}
