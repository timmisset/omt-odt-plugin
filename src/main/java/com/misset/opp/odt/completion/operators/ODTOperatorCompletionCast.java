package com.misset.opp.odt.completion.operators;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyResourceUtil;
import com.misset.opp.odt.builtin.operators.CastOperator;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTOperatorCall;
import com.misset.opp.odt.psi.ODTSignature;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.misset.opp.odt.completion.CompletionPatterns.getInsideBuiltinOperatorSignaturePattern;

public class ODTOperatorCompletionCast extends CompletionContributor {
    public ODTOperatorCompletionCast() {
        extend(CompletionType.BASIC, getInsideBuiltinOperatorSignaturePattern(CastOperator.INSTANCE), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                // get the CommandCall:
                PsiElement element = parameters.getPosition();
                Optional.ofNullable(PsiTreeUtil.getParentOfType(element, ODTSignature.class))
                        .map(odtSignature -> PsiTreeUtil.getParentOfType(odtSignature, ODTOperatorCall.class))
                        .ifPresent(call -> addCastOperatorCompletions(parameters, result, element, call));
            }
        });
    }

    private static void addCastOperatorCompletions(@NotNull CompletionParameters parameters,
                                                   @NotNull CompletionResultSet result,
                                                   PsiElement element,
                                                   ODTOperatorCall operatorCall) {
        int argumentIndexOf = operatorCall.getArgumentIndexOf(element);
        if (argumentIndexOf == 0) {
            ODTFile file = (ODTFile) parameters.getOriginalFile();
            Map<String, String> availableNamespaces = file.getAvailableNamespaces();

            OntologyResourceUtil resourceUtil = OntologyResourceUtil.getInstance(file.getProject());
            // show all classes instances:
            OntologyModelConstants.listXSDTypes().stream().map(
                            resource -> resourceUtil.getRootLookupElement(resource, resourceUtil.describeUriForLookup(resource), availableNamespaces))
                    .filter(Objects::nonNull)
                    .forEach(result::addElement);

            result.addElement(LookupElementBuilder.create("JSON").withTypeText("JSON"));
            result.addElement(LookupElementBuilder.create("IRI").withTypeText("IRI"));

            result.stopHere();
        }
    }
}
