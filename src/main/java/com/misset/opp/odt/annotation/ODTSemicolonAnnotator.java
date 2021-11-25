package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.ODTInjectionUtil;
import com.misset.opp.odt.psi.ODTScriptContent;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class ODTSemicolonAnnotator implements Annotator {

    protected static final String SEMICOLON_REQUIRED = "Semicolon required";
    protected static final String SEMICOLON_ILLEGAL = "Should not contain a semicolon";

    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        if (element instanceof ODTScriptContent) {
            final PsiElement nextVisibleLeaf = PsiTreeUtil.nextVisibleLeaf(element);
            final boolean hasSemicolonEnding = nextVisibleLeaf != null && nextVisibleLeaf.getNode()
                    .getElementType() == ODTTypes.SEMICOLON;
            final YamlMetaType injectionMetaType = ODTInjectionUtil.getInjectionMetaType(element);

            if (injectionMetaType == null) {
                return;
            }

            if (!injectionMetaType.getClass().isAnnotationPresent(SimpleInjectable.class) && !hasSemicolonEnding) {
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
