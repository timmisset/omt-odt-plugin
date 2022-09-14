package com.misset.opp.odt.refactoring;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTStatement;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;

class ODTNameSuggestionProviderTest extends ODTTestCase {

    private ODTNameSuggestionProvider suggestionProvider = new ODTNameSuggestionProvider();
    private LinkedHashSet<String> results;

    @BeforeEach
    void createResultSet() {
        results = new LinkedHashSet<>();
    }

    @Test
    void testGetStringSuggestion() {
        testStatementSuggestedNames("\"string\"", "$string");
    }

    @Test
    void testGetNumberSuggestion() {
        testStatementSuggestedNames("12", "$integer", "$decimal");
    }

    @Test
    void testGetDecimalSuggestion() {
        testStatementSuggestedNames("12.0", "$decimal");
    }

    @Test
    void testGetClassSuggestion() {
        testStatementSuggestedNames("/ont:ClassA / ^rdf:type", "$classA");
        testStatementSuggestedNames("/ont:ClassBSub / ^rdf:type", "$classB", "$classBSub");
    }

    private void testStatementSuggestedNames(String statement, String... expectedNames) {
        String content = withPrefixes("<caret>" + statement + ";");
        configureByText(content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.findElementByText(statement, ODTStatement.class);
            suggestionProvider.getSuggestedNames(elementAtCaret, elementAtCaret, results);
            assertContainsElements(results, expectedNames);
        });
    }
}
