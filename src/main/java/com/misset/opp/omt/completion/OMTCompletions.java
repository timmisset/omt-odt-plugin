package com.misset.opp.omt.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class OMTCompletions extends CompletionContributor {

    public OMTCompletions() {
        extend(CompletionType.BASIC, psiElement(), new OMTKeyCompletion());
    }

}
