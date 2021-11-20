package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.ODTInjectionUtil;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class ODTScriptAnnotator implements Annotator {

    protected static final String SINGLE_STATEMENT_EXPECTED = "Single statement expected";

    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        if (element instanceof ODTScript && ((ODTScript) element).getScriptLineList().size() > 1) {
            final YamlMetaType injectionMetaType = ODTInjectionUtil.getInjectionMetaType(element);
            if (!(injectionMetaType instanceof OMTScriptMetaType)) {
                holder.newAnnotation(HighlightSeverity.ERROR, SINGLE_STATEMENT_EXPECTED).create();
            }
        }
    }
}
