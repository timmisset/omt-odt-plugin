package com.misset.opp.odt.inspection.redundancy;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ODTStyleInspectionNegationOperatorTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTStyleInspectionNegationOperator.class));
    }

    @Test
    void testHasWarningEXISTS() {
        configureByText("IF $variable / NOT EXISTS { }");
        inspection.assertHasWeakWarning(ODTStyleInspectionNegationOperator.WARNING);
    }

    @Test
    void testHasWarningEMPTY() {
        configureByText("IF $variable / NOT EMPTY { }");
        inspection.assertHasWeakWarning(ODTStyleInspectionNegationOperator.WARNING);
    }

    @Test
    void testHasWarningEXISTSTrailing() {
        configureByText("IF $variable / EXISTS / NOT { }");
        inspection.assertHasWeakWarning(ODTStyleInspectionNegationOperator.WARNING);
    }

    @Test
    void testHasNoWarningEXISTS() {
        configureByText("IF $variable / EXISTS { }");
        inspection.assertNoWeakWarning(ODTStyleInspectionNegationOperator.WARNING);
    }

    @Test
    void testHasWarningNotOperator() {
        configureByText("IF NOT($variable / EXISTS) { }");
        inspection.assertHasWeakWarning(ODTStyleInspectionNegationOperator.WARNING);
    }

    @Test
    void testHasNoWarningEMPTY() {
        configureByText("IF $variable / EMPTY { }");
        inspection.assertNoWeakWarning(ODTStyleInspectionNegationOperator.WARNING);
    }

    @Test
    void testReplaceLeadingNegation() {
        configureByText("IF $variable / NOT EMPTY { }");
        inspection.invokeQuickFixIntention(ODTStyleInspectionNegationOperator.REPLACE);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getFile().getText().contains("IF $variable / EXISTS { }")));
    }

    @Test
    void testReplaceInsideNegation() {
        configureByText("IF NOT($variable / EMPTY) { }");
        inspection.invokeQuickFixIntention(ODTStyleInspectionNegationOperator.REPLACE);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getFile().getText().contains("IF $variable / EXISTS { }")));
    }

    @Test
    void testReplaceTrailingNegationEmptyForExists() {
        configureByText("IF $variable / EMPTY / NOT { }");
        inspection.invokeQuickFixIntention(ODTStyleInspectionNegationOperator.REPLACE);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getFile().getText().contains("IF $variable / EXISTS { }")));
    }

    @Test
    void testReplaceTrailingNegationExistsForEmpty() {
        configureByText("IF $variable / EXISTS / NOT { }");
        inspection.invokeQuickFixIntention(ODTStyleInspectionNegationOperator.REPLACE);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getFile().getText().contains("IF $variable / EMPTY { }")));
    }
}
