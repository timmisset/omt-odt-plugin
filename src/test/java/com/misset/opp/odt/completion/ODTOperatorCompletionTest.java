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
        assertContainsElements(lookupStrings, "queryA", "LOG");
        assertDoesntContain(lookupStrings, "queryC");
    }

    @Test
    void testProviders() {
        String content = "queries: |\n" +
                "   DEFINE QUERY queryA => '';\n" +
                "commands: |\n" +
                "   DEFINE COMMAND command => { VAR $variable = <caret> }\n";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "queryA");
    }

    @Test
    void testInsideCallArgument() {
        String content = "queries: |\n" +
                "   DEFINE QUERY queryA => '';\n" +
                "commands: |\n" +
                "   DEFINE COMMAND command => { @LOG(<caret>); }\n";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "queryA", "CURRENT_DATE", "CURRENT_DATETIME");
        assertDoesntContain(lookupStrings, "LOG");
    }

    @Test
    void testInsideCallArgumentHasBuiltinOperatorsOnNextStep() {
        String content = "queries: |\n" +
                "   DEFINE QUERY queryA => '';\n" +
                "commands: |\n" +
                "   DEFINE COMMAND command => { @LOG('' / <caret>); }\n";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "queryA", "LOG");
        assertDoesntContain(lookupStrings, "CURRENT_DATE", "CURRENT_DATETIME");
    }

    @Test
    void testNoBuiltinOperatorsOnVariableAssignment() {
        String content = "queries: |\n" +
                "   DEFINE QUERY queryA => '';\n" +
                "commands: |\n" +
                "   DEFINE COMMAND command => { $variable = <caret>; }\n";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "queryA", "CURRENT_DATE", "CURRENT_DATETIME");
        assertDoesntContain(lookupStrings, "LOG");
    }
}
