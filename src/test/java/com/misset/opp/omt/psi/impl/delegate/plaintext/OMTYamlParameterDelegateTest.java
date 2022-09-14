package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiNamedElement;
import com.misset.opp.omt.testcase.OMTTestCase;
import com.misset.opp.refactoring.SupportsSafeDelete;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTYamlParameterDelegateTest extends OMTTestCase {

    @Test
    void testIsUnusedForUnusedParameter() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- $pa<caret>ram");
        configureByText(content);
        PsiNamedElement delegateAtCaret = getDelegateAtCaret();
        underProgress(() -> ReadAction.run(() -> {
            Assertions.assertTrue(delegateAtCaret instanceof SupportsSafeDelete);
            Assertions.assertTrue(((SupportsSafeDelete) delegateAtCaret).isUnused());
        }));
    }

    @Test
    void testIsNotAvailableForUsedParameter() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- $pa<caret>ram\n" +
                "onStart: |\n" +
                "   @LOG($param);");
        configureByText(content);
        PsiNamedElement delegateAtCaret = getDelegateAtCaret();
        underProgress(() -> ReadAction.run(() -> {
            Assertions.assertTrue(delegateAtCaret instanceof SupportsSafeDelete);
            Assertions.assertFalse(((SupportsSafeDelete) delegateAtCaret).isUnused());
        }));
    }


    @Test
    void testDeleteRemovesParameterAndBlock() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- $pa<caret>ram");
        configureByText(content);
        PsiNamedElement delegateAtCaret = getDelegateAtCaret();
        WriteCommandAction.runWriteCommandAction(getProject(), delegateAtCaret::delete);
        String contentAfterDelete = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals(insideActivityWithPrefixes(""), contentAfterDelete);
    }

    @Test
    void testDeleteRemovesParameterAndKeepsBlock() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- $dontDeleteMePlease\n" +
                "- $pa<caret>ram");
        configureByText(content);
        PsiNamedElement delegateAtCaret = getDelegateAtCaret();
        WriteCommandAction.runWriteCommandAction(getProject(), delegateAtCaret::delete);
        String contentAfterDelete = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals(insideActivityWithPrefixes("params:\n" +
                "- $dontDeleteMePlease\n"), contentAfterDelete);
    }

    @Test
    void testDeleteRemovesParameterFromCall() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- $dontDeleteMePlease\n" +
                "- $pa<caret>ram\n" +
                "\n" +
                "onStart:\n" +
                "   @CallableActivity('persist', 'remove');", "CallableActivity");
        configureByText(content);
        PsiNamedElement delegateAtCaret = getDelegateAtCaret();
        underProgress(() -> WriteCommandAction.runWriteCommandAction(getProject(), delegateAtCaret::delete));
        String contentAfterDelete = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals(insideActivityWithPrefixes("params:\n" +
                "- $dontDeleteMePlease\n" +
                "\n" +
                "onStart:\n" +
                "  @CallableActivity('persist');", "CallableActivity"), contentAfterDelete);
    }

}
