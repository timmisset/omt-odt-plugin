package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class ODTTestCompletion extends CompletionContributor {

    public ODTTestCompletion() {
        extend(CompletionType.BASIC, psiElement(), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                result.addElement(
                        LookupElementBuilder.create("DEFAULT_COMPLETION")
                );
                result.stopHere();
            }
        });
    }

}
