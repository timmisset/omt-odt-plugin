package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableStep;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.util.ODTResolvableUtil;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Annotator of all elements that implement the ODTResolvable interface
 */
public class ODTResolvableAnnotator implements Annotator {
    private static final Logger LOGGER = Logger.getInstance(ODTResolvableAnnotator.class);

    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {

        if (element instanceof ODTResolvable) {
            LoggerUtil.runWithLogger(LOGGER, "Annotating ODTResolvable " + element.getText(), () -> ((ODTResolvable) element).annotate(holder));
        } else if (element instanceof ODTVariable && !(element.getParent() instanceof ODTVariableStep)) {
            LoggerUtil.runWithLogger(LOGGER, "Annotating ODTVariable " + element.getText(), () -> {
                final Set<OntResource> type = ((ODTVariable) element).resolve();
                if (!type.isEmpty()) {
                    ODTResolvableUtil.annotateResolved(type, holder, element, true);
                }
            });
        }
    }
}
