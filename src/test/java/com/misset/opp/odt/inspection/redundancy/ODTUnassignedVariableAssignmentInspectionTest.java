package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTUnassignedVariableAssignmentInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTUnassignedVariableAssignmentInspection.class);
    }

    @Test
    void testHasWarningForUnassignedVariable() {
        String content = insideProcedureRunWithPrefixes("VAR $variable;\n" +
                "@LOG($variable);");
        configureByText(content);
        assertHasWarning("$variable is used before it is assigned a value");
    }

    @Test
    void testHasNoWarningForAssignedVariable() {
        String content = insideProcedureRunWithPrefixes("VAR $variable = 'test';\n" +
                "@LOG($variable);");
        configureByText(content);
        assertNoWarning("$variable is used before it is assigned a value");
    }

    @Test
    void testHasNoWarningWhenGlobalVariable() {
        String content = insideProcedureRunWithPrefixes("@LOG($username);");
        configureByText(content);
        assertNoWarning("$variable is used before it is assigned a value");
    }

    @Test
    void testHasNoWarningWhenOMTVariable() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "- $variable\n" +
                        "onStart:\n" +
                        "   @LOG($variable);");
        configureByText(content);
        assertNoWarning("$variable is used before it is assigned a value");
    }

    @Test
    void testHasNoWarningForReAssignedVariable() {
        String content = insideProcedureRunWithPrefixes("VAR $variable;\n" +
                "$variable = 'test';\n" +
                "@LOG($variable);");
        configureByText(content);
        assertNoWarning("$variable is used before it is assigned a value");
    }

    @Test
    void testHasWarningForReAssignedVariable() {
        String content = insideProcedureRunWithPrefixes("VAR $variable;\n" +
                "@LOG($variable);\n" +
                "$variable = 'test';\n"
        );
        configureByText(content);
        assertHasWarning("$variable is used before it is assigned a value");
    }
}
