package com.misset.opp.odt.inspection.operators;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.InspectionTestCase;
import com.misset.opp.testCase.OMTOntologyTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.operators.ODTOperatorInspectionIIf.COMBINE;
import static com.misset.opp.odt.inspection.operators.ODTOperatorInspectionIIf.SIMPLIFY;
import static com.misset.opp.odt.inspection.operators.ODTOperatorInspectionIIf.UNNECESSARY_IIF;

class ODTOperatorInspectionIIfTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTOperatorInspectionIIf.class);
    }

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        OMTOntologyTestCase.getOntologyModel();
    }

    @Test
    void testIIfOperatorShowsNoWarningWhenNoBooleanSignatureArguments() {
        String content = insideQueryWithPrefixes("" +
                "IIF($condition, 'a', 'b')");
        configureByText(content);
        assertNoWarning(UNNECESSARY_IIF);
    }

    @Test
    void testIIfOperatorShowsNoWarningWhenOneBooleanSignatureArguments() {
        String content = insideQueryWithPrefixes("" +
                "IIF($condition, true, 'b')");
        configureByText(content);
        assertNoWarning(UNNECESSARY_IIF);
    }

    @Test
    void testIIfOperatorShowsNoWarningWhenOneBooleanSignatureArgumentsOnFalse() {
        String content = insideQueryWithPrefixes("" +
                "IIF($condition, 'a', true)");
        configureByText(content);
        assertNoWarning(UNNECESSARY_IIF);
    }

    @Test
    void testIIfOperatorShowsSimplifyWarning() {
        String content = insideQueryWithPrefixes("" +
                "IIF($condition, true, false)");
        configureByText(content);
        assertHasWarning(UNNECESSARY_IIF);
    }

    @Test
    void testIIfOperatorShowsCombineWarning() {
        String content = insideQueryWithPrefixes("" +
                "IIF($condition, $variable == 'a')");
        configureByText(content);
        assertHasWarning(UNNECESSARY_IIF);
    }

    @Test
    void testIIfOperatorRefactorSimplified() {
        String content = insideQueryWithPrefixes("" +
                "IIF($condition, true, false)");
        configureByText(content);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, true, false)"));
        invokeQuickFixIntention(SIMPLIFY);
        Assertions.assertTrue(getFile().getText().contains("$condition"));
        Assertions.assertFalse(getFile().getText().contains("IIF($condition, true, false)"));
    }

    @Test
    void testIIfOperatorRefactorSimplifiedNegated() {
        String content = insideQueryWithPrefixes("" +
                "IIF($condition, false, true)");
        configureByText(content);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, false, true)"));
        invokeQuickFixIntention(SIMPLIFY);
        Assertions.assertTrue(getFile().getText().contains("NOT $condition"));
        Assertions.assertFalse(getFile().getText().contains("IIF($condition, true, false)"));
    }

    @Test
    void testIIfOperatorRefactorSimplifiedNoOtherwise() {
        String content = insideQueryWithPrefixes("" +
                "IIF($condition, true)");
        configureByText(content);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, true)"));
        invokeQuickFixIntention(SIMPLIFY);
        Assertions.assertTrue(getFile().getText().contains("$condition"));
        Assertions.assertFalse(getFile().getText().contains("IIF($condition, true)"));
    }

    @Test
    void testIIfOperatorRefactorSimplifiedNegatedNoOtherwise() {
        String content = insideQueryWithPrefixes("" +
                "IIF($condition, false)");
        configureByText(content);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, false)"));
        invokeQuickFixIntention(SIMPLIFY);
        Assertions.assertTrue(getFile().getText().contains("NOT $condition"));
        Assertions.assertFalse(getFile().getText().contains("IIF($condition, false)"));
    }

    @Test
    void testIIfOperatorRefactorCombine() {
        String content = insideQueryWithPrefixes("" +
                "IIF($condition, $variable == 'a')");
        configureByText(content);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, $variable == 'a')"));
        invokeQuickFixIntention(COMBINE);
        Assertions.assertTrue(getFile().getText().contains("$condition AND $variable == 'a'"));
        Assertions.assertFalse(getFile().getText().contains("IIF($condition, $variable == 'a')"));
    }

}
