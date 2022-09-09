package com.misset.opp.odt.inspection.redundancy;

import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ODTUnusedVariableAssignmentInspectionTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTUnusedVariableAssignmentInspection.class));
    }

    @Test
    void testHasWarningWhenNotUsed() {
        configureByText("VAR $variable = 'test';");
        inspection.assertHasWarning("$variable is assigned but it's value is never read");
    }

    @Test
    void testHasNoWarningWhenUnusedVariable() {
        configureByText("VAR $_variable = 'test';");
        inspection.assertNoWarning("$_variable is assigned but it's value is never read");
    }

    @Test
    void testHasNoWarningWhenDeclaredOutsideScopeVariable() {
        configureByText("$variable = 'test';");
        inspection.assertNoWarning("$variable is assigned but it's value is never read");
    }

    @Test
    void testHasNoWarningWhenUsed() {
        String content = "VAR $variable = 'test'; @LOG($variable);";
        configureByText(content);
        inspection.assertNoWarning("$variable is assigned but it's value is never read");
    }

    @Test
    void testHasNoWarningWhenUsedNested() {
        String content = "VAR $variable = 'test'; IF true { @LOG($variable); }";
        configureByText(content);
        inspection.assertNoWarning("$variable is assigned but it's value is never read");
    }

    @Test
    void testHasWarningWhenSecondNotUsed() {
        String content = "VAR $variable, $variableB = 'test'; @LOG($variable);";
        configureByText(content);
        inspection.assertHasWarning("$variableB is assigned but it's value is never read");
    }
}
