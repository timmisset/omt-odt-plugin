package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import com.misset.opp.testCase.OMTDelegateTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTYamlParameterDelegateTest extends OMTDelegateTestCase {

    @Test
    void testIsUnusedForUnusedParameter() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- $pa<caret>ram");
        configureByText(content);
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
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
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
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
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
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
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
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
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        underProgress(() -> WriteCommandAction.runWriteCommandAction(getProject(), delegateAtCaret::delete));
        String contentAfterDelete = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals(insideActivityWithPrefixes("params:\n" +
                "- $dontDeleteMePlease\n" +
                "\n" +
                "onStart:\n" +
                "  @CallableActivity('persist');", "CallableActivity"), contentAfterDelete);
    }

}
