package com.misset.opp.omt.psi.impl;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTCallableTest extends OMTTestCase {

    @Test
    void testCreateCallable() {
        String content = "model:\n" +
                "   <caret>Activiteit: !Activity\n" +
                "       params:\n" +
                "       - $paramA\n" +
                "       - $paramB\n" +
                "";
        configureByText(content);
        ReadAction.run(() -> {
            final PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof YAMLKeyValue);
            final OMTCallableImpl omtCallable = new OMTCallableImpl((YAMLKeyValue) elementAtCaret);
            Assertions.assertEquals(2, omtCallable.maxNumberOfArguments());
            Assertions.assertEquals(2, omtCallable.minNumberOfArguments());
            Assertions.assertEquals("@Activiteit", omtCallable.getCallId());
            Assertions.assertEquals("Activiteit", omtCallable.getName());
            Assertions.assertTrue(omtCallable.isCommand());
        });
    }

}
