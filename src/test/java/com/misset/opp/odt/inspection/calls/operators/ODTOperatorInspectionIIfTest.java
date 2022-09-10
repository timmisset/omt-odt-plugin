package com.misset.opp.odt.inspection.calls.operators;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.odt.inspection.calls.operators.ODTOperatorInspectionIIf.*;

class ODTOperatorInspectionIIfTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTOperatorInspectionIIf.class));
    }

    @Test
    void testIIfOperatorShowsNoWarningWhenNoBooleanSignatureArguments() {
        String content = "IIF($condition, 'a', 'b')";
        configureByText(content);
        inspection.assertNoWarning(UNNECESSARY_IIF);
    }

    @Test
    void testIIfOperatorShowsNoWarningWhenOneBooleanSignatureArguments() {
        String content = "IIF($condition, true, 'b')";
        configureByText(content);
        inspection.assertNoWarning(UNNECESSARY_IIF);
    }

    @Test
    void testIIfOperatorShowsNoWarningWhenOneBooleanSignatureArgumentsOnFalse() {
        String content = "IIF($condition, 'a', true)";
        configureByText(content);
        inspection.assertNoWarning(UNNECESSARY_IIF);
    }

    @Test
    void testIIfOperatorShowsSimplifyWarning() {
        String content = "IIF($condition, true, false)";
        configureByText(content);
        inspection.assertHasWarning(UNNECESSARY_IIF);
    }

    @Test
    void testIIfOperatorShowsCombineWarning() {
        String content = "IIF($condition, $variable == 'a')";
        configureByText(content);
        inspection.assertHasWarning(UNNECESSARY_IIF);
    }

    @Test
    void testIIfOperatorRefactorSimplified() {
        String content = "IIF($condition, true, false)";
        configureByText(content);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, true, false)"));
        inspection.invokeQuickFixIntention(SIMPLIFY);
        Assertions.assertTrue(getFile().getText().contains("$condition"));
        Assertions.assertFalse(getFile().getText().contains("IIF($condition, true, false)"));
    }

    @Test
    void testIIfOperatorRefactorSimplifiedNegated() {
        String content = "IIF($condition, false, true)";
        configureByText(content);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, false, true)"));
        inspection.invokeQuickFixIntention(SIMPLIFY);
        Assertions.assertTrue(getFile().getText().contains("NOT $condition"));
        Assertions.assertFalse(getFile().getText().contains("IIF($condition, true, false)"));
    }

    @Test
    void testIIfOperatorRefactorSimplifiedNoOtherwise() {
        String content = "IIF($condition, true)";
        configureByText(content);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, true)"));
        inspection.invokeQuickFixIntention(SIMPLIFY);
        Assertions.assertTrue(getFile().getText().contains("$condition"));
        Assertions.assertFalse(getFile().getText().contains("IIF($condition, true)"));
    }

    @Test
    void testIIfOperatorRefactorSimplifiedNegatedNoOtherwise() {
        String content = "IIF($condition, false)";
        configureByText(content);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, false)"));
        inspection.invokeQuickFixIntention(SIMPLIFY);
        Assertions.assertTrue(getFile().getText().contains("NOT $condition"));
        Assertions.assertFalse(getFile().getText().contains("IIF($condition, false)"));
    }

    @Test
    void testIIfOperatorRefactorCombine() {
        String content = "IIF($condition, $variable == 'a')";
        configureByText(content);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, $variable == 'a')"));
        inspection.invokeQuickFixIntention(COMBINE);
        Assertions.assertTrue(getFile().getText().contains("$condition AND $variable == 'a'"));
        Assertions.assertFalse(getFile().getText().contains("IIF($condition, $variable == 'a')"));
    }

}
