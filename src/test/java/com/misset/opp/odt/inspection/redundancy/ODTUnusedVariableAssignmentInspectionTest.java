package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTUnusedVariableAssignmentInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTUnusedVariableAssignmentInspection.class);
    }

    @Test
    void testHasWarningWhenNotUsed() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variable = 'test';"
        );
        configureByText(content);
        assertHasWarning("$variable is assigned but it's value is never read");
    }

    @Test
    void testHasNoWarningWhenUnusedVariable() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $_variable = 'test';"
        );
        configureByText(content);
        assertNoWarning("$_variable is assigned but it's value is never read");
    }

    @Test
    void testHasNoWarningWhenDeclaredOutsideScopeVariable() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "- $variable\n" +
                        "onStart: |\n" +
                        "   $variable = 'test';"
        );
        configureByText(content);
        assertNoWarning("$variable is assigned but it's value is never read");
    }

    @Test
    void testHasNoWarningWhenUsed() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variable = 'test';\n" +
                        "@LOG($variable);"
        );
        configureByText(content);
        assertNoWarning("$variable is assigned but it's value is never read");
    }

    @Test
    void testHasNoWarningWhenUsedNested() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variable = 'test';\n" +
                        "IF true { @LOG($variable); }"
        );
        configureByText(content);
        assertNoWarning("$variable is assigned but it's value is never read");
    }

    @Test
    void testHasWarningWhenSecondNotUsed() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variable, $variableB = 'test';\n" +
                        "@LOG($variable);"
        );
        configureByText(content);
        assertHasWarning("$variableB is assigned but it's value is never read");
    }
}
