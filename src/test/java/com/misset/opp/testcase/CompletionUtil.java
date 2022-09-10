package com.misset.opp.testcase;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CompletionUtil {

    private final JavaCodeInsightTestFixture fixture;

    public CompletionUtil(JavaCodeInsightTestFixture fixture) {
        this.fixture = fixture;
    }

    protected LookupElement[] getLookupElements() {
        return fixture.completeBasic();
    }

    public List<String> getLookupStrings() {
        return Arrays.stream(getLookupElements()).map(
                LookupElement::getLookupString
        ).collect(Collectors.toList());
    }

}
