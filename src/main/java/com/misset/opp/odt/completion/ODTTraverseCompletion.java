package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQueryOperationStep;
import com.misset.opp.ttl.OppModel;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

import static com.intellij.patterns.PsiJavaPatterns.psiElement;

public class ODTTraverseCompletion extends CompletionContributor {

    private enum TraverseDirection {
        FORWARD, REVERSE
    }

    public ODTTraverseCompletion() {
        extend(CompletionType.BASIC, psiElement().inside(ODTQueryStep.class), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {

                Optional.of(parameters.getPosition())
                        .map(element -> PsiTreeUtil.getParentOfType(element, ODTResolvableQueryOperationStep.class))
                        .map(ODTResolvableQueryOperationStep::resolvePreviousStep)
                        .map(subjects -> OppModel.INSTANCE.listPredicates(subjects))
                        .stream()
                        .flatMap(Collection::stream)
                        .map(resource -> LookupElementBuilder.create(resource.toString()))
                        .forEach(result::addElement);
            }
        });
    }

}
