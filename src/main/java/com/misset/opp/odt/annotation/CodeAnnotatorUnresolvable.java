package com.misset.opp.odt.annotation;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTResolvableCall;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.util.OMTImportUtil;
import com.misset.opp.resolvable.Callable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Code annotator for all unused declarations
 */
public class CodeAnnotatorUnresolvable implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element,
                         @NotNull AnnotationHolder holder) {
        if (element instanceof ODTVariable) {
            annotateVariable(holder, (ODTVariable) element);
        } else if (element instanceof ODTResolvableCall) {
            annotateCall(holder, (ODTResolvableCall) element);
        }
    }

    private void annotateVariable(@NotNull AnnotationHolder holder,
                                  @NotNull ODTVariable variable) {
        if (!variable.isDeclaredVariable() && variable.getDeclared() == null) {
            validateReference(holder, variable, String.format("%s is not declared", variable.getText()));
        }
    }

    private void annotateCall(@NotNull AnnotationHolder holder,
                              @NotNull ODTResolvableCall call) {
        final Callable callable = call.getCallable();
        if (callable == null) {
            OMTFile hostFile = call.getContainingFile().getHostFile();
            String error = String.format("%s is not declared", call.getName());
            try {
                IntentionAction[] importIntentions = OMTImportUtil.getImportIntentions(hostFile, call);
                AnnotationBuilder annotationBuilder = holder.newAnnotation(HighlightSeverity.ERROR, error)
                        .range(call.getCallName());
                if (importIntentions != null) {
                    Arrays.stream(importIntentions)
                            .filter(Objects::nonNull)
                            .forEach(annotationBuilder::withFix);
                }
                annotationBuilder.create();
            } catch (ProcessCanceledException canceledException) {
                // searching took too long, still show as error:
                error += "<br>Searching for imports took too long, process was aborted";
                holder.newAnnotation(HighlightSeverity.ERROR, error)
                        .range(call.getCallName()).create();
            }
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
