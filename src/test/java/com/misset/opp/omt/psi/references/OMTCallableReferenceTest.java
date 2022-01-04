package com.misset.opp.omt.psi.references;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTCallableReferenceTest extends OMTTestCase {

    @Test
    void testHasReference() {
        String content = insideActivityWithPrefixes("" +
                "queries:\n" +
                "   DEFINE QUERY query => '';\n" +
                "payload:\n" +
                "   item:\n" +
                "       query: <caret>query");
        configureByText(content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof PsiCallable);
        });
    }

}
