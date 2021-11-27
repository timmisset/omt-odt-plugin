package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.ODTInjectionUtil;
import com.misset.opp.odt.psi.ODTDefineQueryStatement;
import com.misset.opp.odt.psi.ODTScriptContent;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.ODTScriptLineWithSemicolon;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;

/**
 * Semicolon usage is not caught by the Lexer on design. This would mean having to insert
 * it anywhere where completion is triggered to generate a valid completion PsiFile
 */
public class ODTSemicolonAnnotator implements Annotator {

    protected static final String SEMICOLON_REQUIRED = "Semicolon required";
    protected static final String SEMICOLON_ILLEGAL = "Should not contain a semicolon";

    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        if (element instanceof ODTScriptLine) {
            final boolean hasSemicolonEnding = element.getParent() instanceof ODTScriptLineWithSemicolon;

            final ODTScriptLine scriptLine = (ODTScriptLine) element;
            if (scriptLine instanceof ODTDefineQueryStatement && !hasSemicolonEnding) {
                // should have a semicolon
                holder.newAnnotation(HighlightSeverity.ERROR, SEMICOLON_REQUIRED).create();
            } else if (scriptLine instanceof ODTScriptContent) {
                // depends on the location:
                final YamlMetaType injectionMetaType = ODTInjectionUtil.getInjectionMetaType(element);
                if (injectionMetaType == null) {
                    // no meta-type, the content is part of an ODT file, should have a semicolon ending in that case
                    holder.newAnnotation(HighlightSeverity.ERROR, SEMICOLON_REQUIRED).create();
                } else {
                    if (!injectionMetaType.getClass()
                            .isAnnotationPresent(SimpleInjectable.class) && !hasSemicolonEnding) {
                        // should have a semicolon on every scriptline:
                        holder.newAnnotation(HighlightSeverity.ERROR, SEMICOLON_REQUIRED).create();
                    } else if (injectionMetaType.getClass()
                            .isAnnotationPresent(SimpleInjectable.class) && hasSemicolonEnding) {
                        // should not have a semicolon ending:
                        holder.newAnnotation(HighlightSeverity.ERROR, SEMICOLON_ILLEGAL).create();
                    }
                }
            }
        }
    }
}
