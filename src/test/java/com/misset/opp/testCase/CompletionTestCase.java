package com.misset.opp.testCase;

import com.intellij.codeInsight.lookup.LookupElement;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CompletionTestCase extends OMTTestCase {

    protected List<String> getCompletionList(String content) {
        configureByText(content);
        return getCompletionList();
    }
    protected List<String> getCompletionList() {
        final LookupElement[] lookupElements = myFixture.completeBasic();
        return parseSuggestions(lookupElements);
    }

    private List<String> parseSuggestions(LookupElement[] lookupElements) {
        if (lookupElements == null || lookupElements.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.stream(lookupElements).filter(Objects::nonNull).map(LookupElement::getLookupString).collect(
                Collectors.toList());
    }

}
