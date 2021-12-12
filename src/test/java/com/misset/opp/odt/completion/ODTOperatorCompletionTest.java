package com.misset.opp.odt.completion;

import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTOperatorCompletionTest extends OMTCompletionTestCase {

    @Test
    void testBuiltinOperators() {
        configureByText(insideQueryWithPrefixes("<caret>"), true);
        assertContainsElements(getLookupStrings(), "LOG");
    }

    @Test
    void testSiblingOperators() {
        String content = "queries: |\n" +
                "   DEFINE QUERY queryA => '';\n" +
                "   DEFINE QUERY queryB => <caret>;\n" +
                "   DEFINE QUERY queryC => '';\n";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "queryA");
        assertDoesntContain(lookupStrings, "queryC");
    }

    @Test
    void testProviders() {
        String content = "queries: |\n" +
                "   DEFINE QUERY queryA => '';\n" +
                "commands: |\n" +
                "   DEFINE COMMAND command => { <caret> };\n";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "queryA");
    }
}
