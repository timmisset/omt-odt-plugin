package com.misset.opp.odt.psi.wrapping;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.wrapping.impl.ODTDefinedVariableImpl;
import com.misset.opp.odt.psi.wrapping.impl.ODTUsageVariableImpl;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ODTWrappingTest extends OMTTestCase {

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    void testCreateDefinedVariable() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $<caret>variable = 'test';"
        );
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTDefinedVariableImpl);
            Assertions.assertTrue(((ODTVariableWrapper)elementAtCaret).isDefinedVariable());
        });
    }

    @Test
    void testCreateUsageVariable() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variable = $<caret>anotherVariable;"
        );
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof ODTUsageVariableImpl);
            Assertions.assertFalse(((ODTVariableWrapper)elementAtCaret).isDefinedVariable());
        });
    }
}
