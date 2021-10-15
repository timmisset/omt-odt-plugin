package com.misset.opp.odt.psi;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.impl.variables.ODTBaseVariable;
import com.misset.opp.odt.psi.impl.variables.ODTDefinedVariableImpl;
import com.misset.opp.odt.psi.impl.variables.ODTUsageVariableImpl;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ODTFactoryInterceptorTest extends OMTTestCase {

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
            Assertions.assertTrue(((ODTBaseVariable)elementAtCaret).isDefinedVariable());
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
            Assertions.assertFalse(((ODTBaseVariable)elementAtCaret).isDefinedVariable());
        });
    }
}
