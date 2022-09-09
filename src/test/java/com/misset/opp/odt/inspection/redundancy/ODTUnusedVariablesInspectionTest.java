package com.misset.opp.odt.inspection.redundancy;

import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ODTUnusedVariablesInspectionTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTUnusedVariablesInspection.class));
    }

    @Test
    void getStaticDescription() {
        Assertions.assertEquals(ODTUnusedVariablesInspection.DESCRIPTION, new ODTUnusedVariablesInspection().getStaticDescription());
    }

    @Test
    void testHasWarningWhenUnusedVariable() {
        configureByText("VAR $variable = true;");
        inspection.assertHasWarning("$variable is never used");
    }

    @Test
    void testHasNoWarningWhenVariableIsUsed() {
        String content = "VAR $variable = true; @LOG($variable);";
        configureByText(content);
        inspection.assertNoWarning("$variable is never used");
    }

    @Test
    void testRemovesUnusedVariable() {
        String content = "VAR $variable = true;\n" +
                "VAR $usedVariable = true;\n" +
                "@LOG($usedVariable);";
        configureByText(content);
        inspection.invokeQuickFixIntention("Remove $variable");

        Assertions.assertFalse(getFile().getText().contains("VAR $variable = true;"));
    }

}
