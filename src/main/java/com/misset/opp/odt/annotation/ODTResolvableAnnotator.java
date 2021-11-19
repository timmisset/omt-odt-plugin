package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableStep;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.util.ODTResolvableUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Annotate of all elements that implement the ODTResolvable interface
 */
public class ODTResolvableAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        if (element instanceof ODTResolvable) {
            ((ODTResolvable) element).annotate(holder);
        } else if (element instanceof ODTVariable && !(element.getParent() instanceof ODTVariableStep)) {
            final Set<OntResource> type = ((ODTVariable) element).getType();
            if (!type.isEmpty()) {
                ODTResolvableUtil.annotateResolved(type, holder, element, true);
            }
        }
    }
}
