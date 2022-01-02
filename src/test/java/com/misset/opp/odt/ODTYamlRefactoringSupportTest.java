package com.misset.opp.odt;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTYamlRefactoringSupportTest extends OMTTestCase {

    private ODTRefactoringSupport refactoringSupport = new ODTRefactoringSupport();

    @Test
    void testIsAvailable() {
        String content = insideProcedureRunWithPrefixes("@LOG($<caret>test);");
        configureByText(content);
        PsiElement psiElement = ReadAction.compute(myFixture::getElementAtCaret);
        Assertions.assertTrue(refactoringSupport.isAvailable(psiElement));
    }

    @Test
    void testIsSafeDeleteAvailable() {
        String content = insideProcedureRunWithPrefixes("VAR <caret>$test;");
        configureByText(content);
        PsiElement psiElement = ReadAction.compute(myFixture::getElementAtCaret);
        underProgress(() -> ReadAction.run(() -> {
            Assertions.assertTrue(refactoringSupport.isSafeDeleteAvailable(psiElement));
        }));
    }

    @Test
    void testIsInplaceRenameAvailable() {
        String content = insideProcedureRunWithPrefixes("VAR <caret>$test;");
        configureByText(content);
        PsiElement psiElement = ReadAction.compute(myFixture::getElementAtCaret);
        Assertions.assertTrue(refactoringSupport.isInplaceRenameAvailable(null, psiElement));
    }
}
