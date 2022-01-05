package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

public class ODTDocumentationAnnotator implements Annotator {
    private static final Logger LOGGER = Logger.getInstance(ODTDocumentationAnnotator.class);


    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
//        if (element instanceof ODTDocumented) {
//            LoggerUtil.runWithLogger(LOGGER, "Annotate documentation for " + element.getText(), () -> {
//                ODTDocumented documented = (ODTDocumented) element;
//                String documentation = (documented).getDocumentation();
//                if (documentation != null && !documentation.isBlank()) {
//                    holder.newAnnotation(HighlightSeverity.INFORMATION, documentation)
//                            .range(documented)
//                            .tooltip(documentation).create();
//                }
//            });
//        }
    }
}
