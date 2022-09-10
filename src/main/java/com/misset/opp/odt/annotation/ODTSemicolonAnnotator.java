package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTInterpolation;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.ODTTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Semicolon usage is not caught by the Lexer by design. This would mean having to insert
 * it anywhere where completion is triggered to generate a valid completion PsiFile
 */
public class ODTSemicolonAnnotator implements Annotator {
    protected static final String SEMICOLON_REQUIRED = "Semicolon required";
    protected static final String SEMICOLON_ILLEGAL = "Should not contain a semicolon";

    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        IElementType elementType = PsiUtilCore.getElementType(element);
        if (elementType == ODTTypes.SCRIPT_LINE) {
            annotateScriptLine((ODTScriptLine) element, holder);
        } else if (elementType == ODTTypes.SEMICOLON &&
                PsiUtilCore.getElementType(PsiTreeUtil.prevVisibleLeaf(element)) == ODTTypes.SEMICOLON) {
            holder.newAnnotation(HighlightSeverity.ERROR, SEMICOLON_ILLEGAL).create();
        }
    }

    private void annotateScriptLine(ODTScriptLine scriptLine, AnnotationHolder holder) {
        final boolean hasSemicolonEnding = hasSemicolonEnding(scriptLine);
        if (PsiTreeUtil.getParentOfType(scriptLine, ODTInterpolation.class) != null ||
                scriptLine.getStatement() == null) {
            validateHasNoIllegalSemicolon(hasSemicolonEnding, holder);
        } else {
            // depends on the context:
            PsiFile containingFile = scriptLine.getContainingFile();
            if (containingFile instanceof ODTFile) {
                if (((ODTFile) containingFile).isStatement()) {
                    validateHasNoIllegalSemicolon(hasSemicolonEnding, holder);
                } else {
                    validateHasRequiredSemicolon(hasSemicolonEnding, holder);
                }
            }
        }
    }

    private boolean hasSemicolonEnding(PsiElement element) {
        return element.getLastChild() != null && element.getLastChild().getNode()
                .getElementType() == ODTTypes.SEMICOLON;
    }

    private void validateHasNoIllegalSemicolon(boolean hasSemicolonEnding, AnnotationHolder holder) {
        if (hasSemicolonEnding) {
            holder.newAnnotation(HighlightSeverity.ERROR, SEMICOLON_ILLEGAL).create();
        }
    }

    private void validateHasRequiredSemicolon(boolean hasSemicolonEnding, AnnotationHolder holder) {
        if (!hasSemicolonEnding) {
            holder.newAnnotation(HighlightSeverity.ERROR, SEMICOLON_REQUIRED).create();
        }
    }
}
