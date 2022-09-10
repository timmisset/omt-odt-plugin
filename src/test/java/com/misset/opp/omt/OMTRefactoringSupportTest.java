package com.misset.opp.omt;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTRefactoringSupportTest extends OMTTestCase {

    private final OMTRefactoringSupport refactoringSupport = new OMTRefactoringSupport();

    @Test
    void testIsAvailableForUnusedParameter() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- $pa<caret>ram");
        configureByText(content);
        underProgress(() -> ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(refactoringSupport.isSafeDeleteAvailable(elementAtCaret));
        }));
    }

    @Test
    void testIsNotAvailableForUsedParameter() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- $pa<caret>ram\n" +
                "onStart: |\n" +
                "   @LOG($param);");
        configureByText(content);
        underProgress(() -> ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertFalse(refactoringSupport.isSafeDeleteAvailable(elementAtCaret));
        }));
    }

}
