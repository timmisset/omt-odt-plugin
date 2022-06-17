package com.misset.opp.odt.completion;

import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTCommandCompletionTest extends OMTCompletionTestCase {

    @Test
    void testBuiltinCommands() {
        configureByText(insideProcedureRunWithPrefixes("<caret>"), true);
        assertContainsElements(getLookupStrings(), "@LOG()");
    }

    @Test
    void testBuiltinCommandsWithPrefix() {
        configureByText(insideProcedureRunWithPrefixes("@<caret>"), true);
        assertContainsElements(getLookupStrings(), "@LOG()");
    }

    @Test
    void testSiblingCommands() {
        String content = "commands: |\n" +
                "   DEFINE COMMAND commandA => { }\n" +
                "   DEFINE COMMAND commandB => { <caret> }\n" +
                "   DEFINE COMMAND commandC => { }\n";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "@commandA()");
        assertDoesntContain(lookupStrings, "@commandC()");
    }

    @Test
    void testNotInsideQuery() {
        String content = "commands: |\n" +
                "   DEFINE COMMAND commandA => { }\n" +
                "   DEFINE COMMAND commandB => { '' / <caret> }\n";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertDoesntContain(lookupStrings, "@commandA()");
    }

    @Test
    void testCanBeInsideCommandCallArgument() {
        String content = "commands: |\n" +
                "   DEFINE COMMAND commandA => { }\n" +
                "   DEFINE COMMAND commandB => { @LOG(<caret>) }\n";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "@commandA()");
    }

    @Test
    void testCanBeVariableAssignment() {
        String content = "commands: |\n" +
                "   DEFINE COMMAND commandA => { }\n" +
                "   DEFINE COMMAND commandB => { VAR $variable = <caret> }\n";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "@commandA()");
    }

    @Test
    void testProviders() {
        String content =
                "commands: |\n" +
                        "   DEFINE COMMAND commandA => { };\n" +
                        "model:\n" +
                        "   Procedure: !Procedure\n" +
                        "       onRun: \n" +
                        "           <caret>";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "@commandA()");
    }

    @Test
    void testLocalCommandsProcedure() {
        String content = "model:\n" +
                "   Procedure: !Procedure\n" +
                "       onRun: \n" +
                "           <caret>";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "@COMMIT()", "@ROLLBACK()");
    }

    @Test
    void testLocalCommandsActivity() {
        String content = "model:\n" +
                "   Activity: !Activity\n" +
                "       onStart: \n" +
                "           <caret>";
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "@COMMIT()", "@ROLLBACK()", "@DONE()", "@CANCEL()");
    }
}
