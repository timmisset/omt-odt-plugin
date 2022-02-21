package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.application.ReadAction;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTStyleInspectionDoubleNegationOperatorTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTStyleInspectionDoubleNegationOperator.class);
    }

    @Test
    void testHasWarningEXISTS() {
        String content = insideProcedureRunWithPrefixes("IF $variable / NOT EXISTS { }");
        configureByText(content);
        assertHasWeakWarning(ODTStyleInspectionDoubleNegationOperator.WARNING);
    }

    @Test
    void testHasWarningEMPTY() {
        String content = insideProcedureRunWithPrefixes("IF $variable / NOT EMPTY { }");
        configureByText(content);
        assertHasWeakWarning(ODTStyleInspectionDoubleNegationOperator.WARNING);
    }

    @Test
    void testHasWarningEXISTSTrailing() {
        String content = insideProcedureRunWithPrefixes("IF $variable / EXISTS / NOT { }");
        configureByText(content);
        assertHasWeakWarning(ODTStyleInspectionDoubleNegationOperator.WARNING);
    }

    @Test
    void testHasNoWarningEXISTS() {
        String content = insideProcedureRunWithPrefixes("IF $variable / EXISTS { }");
        configureByText(content);
        assertNoWeakWarning(ODTStyleInspectionDoubleNegationOperator.WARNING);
    }

    @Test
    void testHasWarningNotOperator() {
        String content = insideProcedureRunWithPrefixes("IF NOT($variable / EXISTS) { }");
        configureByText(content);
        assertHasWeakWarning(ODTStyleInspectionDoubleNegationOperator.WARNING);
    }

    @Test
    void testHasNoWarningEMPTY() {
        String content = insideProcedureRunWithPrefixes("IF $variable / EMPTY { }");
        configureByText(content);
        assertNoWeakWarning(ODTStyleInspectionDoubleNegationOperator.WARNING);
    }

    @Test
    void testReplaceKeywordEXISTS() {
        String content = insideProcedureRunWithPrefixes("IF $variable / NOT EXISTS { }");
        configureByText(content);
        invokeQuickFixIntention(ODTStyleInspectionDoubleNegationOperator.REPLACE);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getFile().getText().contains("IF $variable / EMPTY { }")));
    }

    @Test
    void testReplaceLeadingNegation() {
        String content = insideProcedureRunWithPrefixes("IF $variable / NOT EMPTY { }");
        configureByText(content);
        invokeQuickFixIntention(ODTStyleInspectionDoubleNegationOperator.REPLACE);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getFile().getText().contains("IF $variable / EXISTS { }")));
    }

    @Test
    void testReplaceInsideNegation() {
        String content = insideProcedureRunWithPrefixes("IF NOT($variable / EMPTY) { }");
        configureByText(content);
        invokeQuickFixIntention(ODTStyleInspectionDoubleNegationOperator.REPLACE);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getFile().getText().contains("IF $variable / EXISTS { }")));
    }

    @Test
    void testReplaceTrailingNegation() {
        String content = insideProcedureRunWithPrefixes("IF $variable / EMPTY / NOT { }");
        configureByText(content);
        invokeQuickFixIntention(ODTStyleInspectionDoubleNegationOperator.REPLACE);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getFile().getText().contains("IF $variable / EXISTS { }")));
    }
}
