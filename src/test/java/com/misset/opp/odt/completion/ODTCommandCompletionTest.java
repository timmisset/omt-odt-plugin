package com.misset.opp.odt.completion;

import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTCommandCompletionTest extends ODTTestCase {

    @Test
    void testBuiltinCommands() {
        configureByText("<caret>", true);
        assertContainsElements(completion.getLookupStrings(), "@LOG()");
    }

    @Test
    void testBuiltinCommandsWithPrefix() {
        configureByText("@<caret>", true);
        assertContainsElements(completion.getLookupStrings(), "@LOG()");
    }

    @Test
    void testSiblingCommands() {
        String content =
                "   DEFINE COMMAND commandA => { }\n" +
                        "   DEFINE COMMAND commandB => { <caret> }\n" +
                        "   DEFINE COMMAND commandC => { }\n";
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "@commandA()");
        assertDoesntContain(lookupStrings, "@commandC()");
    }

    @Test
    void testNotInsideQuery() {
        String content =
                "   DEFINE COMMAND commandA => { }\n" +
                        "   DEFINE COMMAND commandB => { '' / <caret> }\n";
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertDoesntContain(lookupStrings, "@commandA()");
    }

    @Test
    void testCanBeInsideCommandCallArgument() {
        String content =
                "   DEFINE COMMAND commandA => { }\n" +
                        "   DEFINE COMMAND commandB => { @LOG(<caret>) }\n";
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "@commandA()");
    }

    @Test
    void testCanBeVariableAssignment() {
        String content =
                "   DEFINE COMMAND commandA => { }\n" +
                        "   DEFINE COMMAND commandB => { VAR $variable = <caret> }\n";
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "@commandA()");
    }
}
