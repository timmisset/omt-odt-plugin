package com.misset.opp.omt.meta.actions;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTActionMetaTypeTest extends OMTTestCase {

    @Test
    void testGetVariableMap() {

        String content = insideActivityWithPrefixes("actions:\n" +
                "   myAction:\n" +
                "       params:\n" +
                "       - $paramA\n" +
                "       onSelect:\n" +
                "           @LOG($<caret>paramA);\n" +
                "");
        configureByText(content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof YAMLPlainTextImpl);
        });

    }
}