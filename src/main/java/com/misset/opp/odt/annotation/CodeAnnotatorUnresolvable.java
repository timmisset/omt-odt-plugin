package com.misset.opp.odt.annotation;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Callable;
import com.misset.opp.odt.psi.ODTNamespacePrefix;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTBaseCall;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Code annotator for all unused declarations
 */
public class CodeAnnotatorUnresolvable implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        if (element instanceof ODTNamespacePrefix) {
            inspectNamespacePrefix(holder, (ODTNamespacePrefix) element);
        } else if (element instanceof ODTVariable) {
            inspectVariable(holder, (ODTVariable) element);
        } else if (element instanceof ODTBaseCall) {
            inspectCall(holder, (ODTBaseCall) element);
        }
    }

    private void inspectVariable(@NotNull AnnotationHolder holder,
                                 @NotNull ODTVariable variable) {
        if (!variable.isDeclaredVariable()) {
            validateReference(holder, variable, String.format("%s is not declared", variable.getText()));
        }
    }

    private void inspectNamespacePrefix(@NotNull AnnotationHolder holder,
                                        @NotNull ODTNamespacePrefix namespacePrefix) {
        // todo: validate namespace prefix
    }

    private void inspectCall(@NotNull AnnotationHolder holder,
                             @NotNull ODTBaseCall call) {
        final Callable callable = call.getCallable();
        if(callable == null) {
            holder.newAnnotation(HighlightSeverity.ERROR, String.format("%s is not declared", call.getName()) )
                    .range(call.getCallName())
                    .create();
        }
    }

    protected void validateReference(@NotNull AnnotationHolder holder,
                                     @NotNull PsiElement element,
                                     @NotNull String errorMessage,
                                     IntentionAction... localQuickFix) {
        if (element.getReference() != null &&
                (element.getReference().resolve() == null ||
                        element.getReference().resolve() == element)) {
            final AnnotationBuilder annotationBuilder = holder.newAnnotation(HighlightSeverity.ERROR, errorMessage)
                    .range(element);
            Arrays.stream(localQuickFix).forEach(annotationBuilder::withFix);
            annotationBuilder.create();
        }
    }

}
