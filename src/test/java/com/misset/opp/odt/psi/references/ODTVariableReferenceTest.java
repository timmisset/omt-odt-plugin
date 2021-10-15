package com.misset.opp.odt.psi.references;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.impl.variables.ODTDefinedVariableImpl;
import com.misset.opp.odt.psi.impl.variables.ODTUsageVariableImpl;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTVariableReferenceTest extends OMTTestCase {

    @Test
    void testODTReferenceCanResolve() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variable = 'test';\n" +
                        "@LOG($<caret>variable)"
        );
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            // is resolved to the defined variable
            Assertions.assertTrue(elementAtCaret instanceof ODTDefinedVariableImpl);
        });
    }

    @Test
    void testODTReferenceCannotResolve() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variableA = 'test';\n" +
                        "@LOG($<caret>variableB)"
        );
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            // is not resolved to the defined variable, returns the $variableB usage variable as named element
            Assertions.assertTrue(elementAtCaret instanceof ODTUsageVariableImpl);
            final PsiReference reference = elementAtCaret.getReference();
            Assertions.assertNotNull(reference);
            Assertions.assertNull(reference.resolve());
        });
    }

    @Test
    void testODTReferenceToOMT() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "- $test\n" +
                        "payload:\n" +
                        "   test: $<caret>test\n"
        );
        configureByText(content);
        ReadAction.run(() -> {
            // the getElementAtCaret method returns the result of resolving the reference of the element at the caret
            // in this case, it should return OMT variable: $test defined in the variables block
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            // is resolved to the OMT variable
            Assertions.assertTrue(elementAtCaret instanceof YAMLSequenceItem);
        });
    }
}
