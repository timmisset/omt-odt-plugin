package com.misset.opp.odt.completion;

import com.misset.opp.odt.ODTFileTestImpl;
import com.misset.opp.odt.ODTTestCase;
import com.misset.opp.resolvable.Variable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ODTVariableCompletionTest extends ODTTestCase {

    @Test
    void testGlobalVariables() {
        String content = withPrefixes(
                "VAR $variableA;\n" +
                        "VAR $anotherVariable = <caret>");
        configureByText(content, true);
        assertContainsElements(completion.getLookupStrings(), "$username");
    }

    @Test
    void testNoVariablesAfterFirstQueryStep() {
        String content = withPrefixes(
                "VAR $variableA;\n" +
                        "VAR $anotherVariable = '' / <caret>");
        configureByText(content, true);
        Assertions.assertTrue(completion.getLookupStrings().stream().noneMatch(s -> s.startsWith("$")));
    }

    @Test
    void testODTVariables() {
        String content = withPrefixes(
                "VAR $variableA;\n" +
                        "VAR $anotherVariable = <caret>");
        configureByText(content, true);
        assertContainsElements(completion.getLookupStrings(), "$variableA");
    }

    @Test
    void testLocalCallVariables() {
        String content = withPrefixes(
                "@FOREACH('', { @LOG(<caret>) });");
        configureByText(content, true);
        assertContainsElements(completion.getLookupStrings(), "$value", "$index", "$array");
    }

    @Test
    void testCallArgumentFiltering() {
        String content = withPrefixes(
                "" +
                        "VAR $stringCollection = 'A' | 'B'\n" +
                        "VAR $stringVariable = '';\n" +
                        "VAR $booleanVariable = true;\n" +
                        "@ADD_TO($stringCollection, <caret>);"
        );
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "$stringVariable");
        assertDoesntContain(lookupStrings, "$booleanVariable");
    }

    @Test
    void testLocalCallVariablesNotOutsideScope() {
        String content = withPrefixes(
                "@FOREACH('', { @LOG($newValue) });" +
                        "@LOG(<caret>);");
        configureByText(content, true);
        assertDoesntContain(completion.getLookupStrings(), "$value", "$index", "$array");
    }

    @Test
    void testExternalDeclaredVariables() {
        String content = "@LOG(<caret>);";
        ODTFileTestImpl file = configureByText(content, true);
        Variable variable = mock(Variable.class);
        doReturn("$variable").when(variable).getName();
        file.addVariable(variable);
        assertContainsElements(completion.getLookupStrings(), "$variable");
    }

}
