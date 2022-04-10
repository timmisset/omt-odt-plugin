package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTUnusedVariablesInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTUnusedVariablesInspection.class);
    }

    @Test
    void getStaticDescription() {
        Assertions.assertEquals(ODTUnusedVariablesInspection.DESCRIPTION, new ODTUnusedVariablesInspection().getStaticDescription());
    }

    @Test
    void testHasWarningWhenUnusedVariable() {
        String content = insideProcedureRunWithPrefixes("VAR $variable = true;");
        configureByText(content);
        assertHasWarning("$variable is never used");
    }

    @Test
    void testHasNoWarningWhenVariableIsUsed() {
        String content = insideProcedureRunWithPrefixes("VAR $variable = true;\n" +
                "@LOG($variable);");
        configureByText(content);
        assertNoWarning("$variable is never used");
    }

    @Test
    void testRemovesUnusedVariable() {
        String content = insideProcedureRunWithPrefixes("VAR $variable = true;\n" +
                "VAR $usedVariable = true;\n" +
                "@LOG($usedVariable);");
        configureByText(content);
        invokeQuickFixIntention("Remove $variable");

        Assertions.assertFalse(getFile().getText().contains("VAR $variable = true;"));
    }

}
