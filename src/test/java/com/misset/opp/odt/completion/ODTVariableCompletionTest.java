package com.misset.opp.odt.completion;

import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Test;

class ODTVariableCompletionTest extends OMTCompletionTestCase {

    @Test
    void testGlobalVariables() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variableA;\n" +
                        "VAR $anotherVariable = <caret>");
        configureByText(content, true);
        assertContainsElements(getLookupStrings(), "$username");
    }

    @Test
    void testODTVariables() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variableA;\n" +
                        "VAR $anotherVariable = <caret>");
        configureByText(content, true);
        assertContainsElements(getLookupStrings(), "$variableA");
    }

    @Test
    void testLocalCallVariables() {
        String content = insideProcedureRunWithPrefixes(
                "@FOREACH('', { @LOG(<caret>) });");
        configureByText(content, true);
        assertContainsElements(getLookupStrings(), "$value", "$index", "$array");
    }

    @Test
    void testLocalCallVariablesNotOutsideScope() {
        String content = insideProcedureRunWithPrefixes(
                "@FOREACH('', { @LOG($newValue) });" +
                        "@LOG(<caret>);");
        configureByText(content, true);
        assertDoesntContain(getLookupStrings(), "$value", "$index", "$array");
    }

    @Test
    void testHostProvidedDeclaredVariables() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "- $variable\n" +
                        "params:\n" +
                        "- $param\n" +
                        "onStart: \n" +
                        "   @LOG(<caret>);");
        configureByText(content, true);
        assertContainsElements(getLookupStrings(), "$variable", "$param");
    }

    @Test
    void testHostProvidedLocalVariables() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "-  name: $test\n" +
                        "   onChange: |\n" +
                        "       @LOG(<caret>)\n");
        configureByText(content, true);
        assertContainsElements(getLookupStrings(), "$newValue", "$oldValue");
    }


}
