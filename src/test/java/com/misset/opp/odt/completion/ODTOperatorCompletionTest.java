package com.misset.opp.odt.completion;

import com.misset.opp.odt.ODTFileTestImpl;
import com.misset.opp.odt.ODTTestCase;
import com.misset.opp.resolvable.Callable;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.when;

class ODTOperatorCompletionTest extends ODTTestCase {

    @Test
    void testRootIncludesOperatorsThatDontRequireInput() {
        String content = "<caret>";
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "CURRENT_DATE", "CURRENT_DATETIME");
    }

    @Test
    void testIncludesExternalOperators() {
        String content = "@LOG(<caret>);";
        ODTFileTestImpl odtFileTest = configureByText(content, true);
        Callable callable = Mockito.mock(Callable.class);
        when(callable.getCallId()).thenReturn("callId");
        when(callable.getName()).thenReturn("callId");
        when(callable.requiresInput()).thenReturn(false);
        odtFileTest.addCallable(callable);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "callId");
    }

    @Test
    void testSiblingOperators() {
        String content =
                "   DEFINE QUERY queryA => '';\n" +
                        "   DEFINE QUERY queryB => <caret>;\n" +
                        "   DEFINE QUERY queryC => '';\n";
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "queryA", "LOG");
        assertDoesntContain(lookupStrings, "queryC");
    }

    @Test
    void testInsideCallArgumentOnlyShowsOperatorsWithoutRequiredInput() {
        String content = "@LOG(<caret>);";
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "CURRENT_DATE", "CURRENT_DATETIME");
        assertDoesntContain(lookupStrings, "LOG");
    }

    @Test
    void testInsideCallArgumentHasBuiltinOperatorsOnNextStep() {
        String content = "@LOG('' / <caret>);";
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "LOG");
        assertDoesntContain(lookupStrings, "CURRENT_DATE", "CURRENT_DATETIME");
    }

    @Test
    void testNoBuiltinOperatorsOnVariableAssignment() {
        String content = "$variable = <caret>;";
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "CURRENT_DATE", "CURRENT_DATETIME");
        assertDoesntContain(lookupStrings, "LOG");
    }
}
