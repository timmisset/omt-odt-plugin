package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.PlatformIcons;
import com.misset.opp.callable.Callable;

public abstract class ODTCallCompletion extends CompletionContributor {

    protected LookupElement getLookupElement(Callable callable) {
        StringBuilder stringBuilder = new StringBuilder();
        if (callable.isCommand()) {
            stringBuilder.append("@");
        }
        stringBuilder.append(callable.getCallId());

        if (callable.minNumberOfArguments() > 0) {
            stringBuilder.append("(");
            for (int i = 0; i < callable.maxNumberOfArguments(); i++) {
                if (i > 0) {
                    stringBuilder.append(", ");
                }
                stringBuilder.append("$param").append(i);
            }
            stringBuilder.append(")");
        }
        return LookupElementBuilder.create(stringBuilder.toString())
                .withLookupString(callable.getCallId())
                .withLookupString(callable.getCallId().toLowerCase())
                .withLookupString(callable.getName())
                .withLookupString(callable.getName().toLowerCase())
                .withIcon(PlatformIcons.METHOD_ICON)
                .withTypeText(callable.getType());
    }

}
