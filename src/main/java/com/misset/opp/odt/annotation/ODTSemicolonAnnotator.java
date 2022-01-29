package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.misset.opp.odt.ODTInjectionUtil;
import com.misset.opp.odt.psi.ODTInterpolation;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;

/**
 * Semicolon usage is not caught by the Lexer by design. This would mean having to insert
 * it anywhere where completion is triggered to generate a valid completion PsiFile
 */
public class ODTSemicolonAnnotator implements Annotator {
    private static final Logger LOGGER = Logger.getInstance(ODTSemicolonAnnotator.class);

    protected static final String SEMICOLON_REQUIRED = "Semicolon required";
    protected static final String SEMICOLON_ILLEGAL = "Should not contain a semicolon";
    protected static final String SEMICOLON_UNNECESSARY = "Unnecessary semicolon";

    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        IElementType elementType = PsiUtilCore.getElementType(element);
        if (elementType == ODTTypes.SCRIPT_LINE) {
            LoggerUtil.runWithLogger(LOGGER, "Annotate scriptline", () -> annotateScriptLine(element, holder));
        } else if (elementType == ODTTypes.SEMICOLON) {
            LoggerUtil.runWithLogger(LOGGER, "Annotate semicolon", () -> {
                PsiElement prevVisibleLeaf = PsiTreeUtil.prevVisibleLeaf(element);
                if (prevVisibleLeaf != null && prevVisibleLeaf.getNode().getElementType() == ODTTypes.SEMICOLON) {
                    holder.newAnnotation(HighlightSeverity.WARNING, SEMICOLON_UNNECESSARY).create();
                }
            });
        }
    }

    private void annotateScriptLine(PsiElement element, AnnotationHolder holder) {
        final boolean hasSemicolonEnding = hasSemicolonEnding(element);
        final ODTScriptLine scriptLine = (ODTScriptLine) element;
        if (PsiTreeUtil.getParentOfType(scriptLine, ODTInterpolation.class) != null) {
            if (hasSemicolonEnding) {
                holder.newAnnotation(HighlightSeverity.ERROR, SEMICOLON_ILLEGAL).create();
            }
        } else if (scriptLine.getStatement() == null) {
            // only statements should ever end with a semicolon:
            if (hasSemicolonEnding) {
                holder.newAnnotation(HighlightSeverity.ERROR, SEMICOLON_ILLEGAL).create();
            }
        } else {
            // depends on the context:
            final YamlMetaType injectionMetaType = ODTInjectionUtil.getInjectionMetaType(element);
            if (injectionMetaType == null) {
                if (!hasSemicolonEnding) {
                    // no meta-type, the content is part of an ODT file, should have a semicolon ending in that case
                    holder.newAnnotation(HighlightSeverity.ERROR, SEMICOLON_REQUIRED).create();
                }
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

    private boolean hasSemicolonEnding(PsiElement element) {
        return element.getLastChild() != null && element.getLastChild().getNode()
                .getElementType() == ODTTypes.SEMICOLON;
    }
}
