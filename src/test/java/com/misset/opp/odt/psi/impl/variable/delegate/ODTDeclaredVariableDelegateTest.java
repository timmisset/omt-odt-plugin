package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTDeclaredVariableDelegateTest extends ODTTestCase {

    @Test
    void testDeleteDeclareVariable() {
        String content = "VAR <caret>$x = '12';\n" +
                "@LOG('test');";
        configureByText(content);
        PsiElement elementAtCaret = ReadAction.compute(myFixture::getElementAtCaret);
        WriteCommandAction.runWriteCommandAction(getProject(), elementAtCaret::delete);
        String contentAfterDelete = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals("@LOG('test');", contentAfterDelete);
    }

}
