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
            ODTDocumented documented = (ODTDocumented) element;
            String documentation = (documented).getDocumentation();
            if (documentation != null && !documentation.isBlank()) {
                holder.newAnnotation(HighlightSeverity.INFORMATION, documentation)
                        .range(documented.getDocumentationElement())
                        .tooltip(documentation).create();
            }
        }
    }
}
