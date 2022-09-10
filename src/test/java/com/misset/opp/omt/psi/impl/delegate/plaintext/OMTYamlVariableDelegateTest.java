package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTYamlVariableDelegateTest extends OMTTestCase {

    @Test
    void testSetNameWithoutSymbol() {
        configureByText(insideActivityWithPrefixes("variables:\n" +
                "- $<caret>test"));
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            delegateAtCaret.setName("newName");
        });
        String content = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals(insideActivityWithPrefixes("variables:\n" +
                "- $newName"), content);
    }

    @Test
    void testSetNameWithSymbol() {
        configureByText(insideActivityWithPrefixes("variables:\n" +
                "- $<caret>test"));
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            delegateAtCaret.setName("$newName");
        });
        String content = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals(insideActivityWithPrefixes("variables:\n" +
                "- $newName"), content);
    }

    @Test
    void testIsParameter() {
        configureByText(insideActivityWithPrefixes("variables:\n" +
                "- $<caret>test"));
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        Assertions.assertTrue(delegateAtCaret instanceof OMTYamlVariableDelegate);
        Assertions.assertFalse(((OMTYamlVariableDelegate) delegateAtCaret).isParameter());
    }

    @Test
    void testIsReadonlyFalse() {
        configureByText(insideActivityWithPrefixes("variables:\n" +
                "- $<caret>test"));
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        Assertions.assertTrue(delegateAtCaret instanceof OMTYamlVariableDelegate);
        ReadAction.run(() -> Assertions.assertFalse(((OMTYamlVariableDelegate) delegateAtCaret).isReadonly()));
    }

    @Test
    void testIsReadonlyFalseSpecified() {
        configureByText(insideActivityWithPrefixes("variables:\n" +
                "- name: $<caret>test\n" +
                "  readonly: false"));
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        Assertions.assertTrue(delegateAtCaret instanceof OMTYamlVariableDelegate);
        ReadAction.run(() -> Assertions.assertFalse(((OMTYamlVariableDelegate) delegateAtCaret).isReadonly()));
    }

    @Test
    void testIsReadonlyTrueSpecified() {
        configureByText(insideActivityWithPrefixes("variables:\n" +
                "- name: $<caret>test\n" +
                "  readonly: true"));
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        Assertions.assertTrue(delegateAtCaret instanceof OMTYamlVariableDelegate);
        ReadAction.run(() -> Assertions.assertTrue(((OMTYamlVariableDelegate) delegateAtCaret).isReadonly()));
    }

    @Test
    void testIsUnusedTrue() {
        configureByText(insideActivityWithPrefixes("variables:\n" +
                "- $<caret>test"));
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        underProgress(() -> {
            ReadAction.run(() -> Assertions.assertTrue(((OMTYamlVariableDelegate) delegateAtCaret).isUnused()));
        });
    }

    @Test
    void testIsUnusedFalse() {
        configureByText(insideActivityWithPrefixes("variables:\n" +
                "- $<caret>test\n" +
                "onStart:\n" +
                "   LOG($test);"));
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        underProgress(() -> {
            ReadAction.run(() -> Assertions.assertFalse(((OMTYamlVariableDelegate) delegateAtCaret).isUnused()));
        });
    }

    @Test
    void testDeleteRemovesSequenceItem() {
        configureByText(insideActivityWithPrefixes("variables:\n" +
                "- $<caret>test\n" +
                "- $another"));
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        WriteCommandAction.runWriteCommandAction(getProject(), delegateAtCaret::delete);
        String contentAfterDelete = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals(insideActivityWithPrefixes("variables:\n" +
                "  - $another"), contentAfterDelete);
    }

    @Test
    void testDeleteRemovesSecondSequenceItem() {
        configureByText(insideActivityWithPrefixes("variables:\n" +
                "- $test\n" +
                "- $<caret>another"));
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        WriteCommandAction.runWriteCommandAction(getProject(), delegateAtCaret::delete);
        String contentAfterDelete = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals(insideActivityWithPrefixes("variables:\n" +
                "- $test\n"), contentAfterDelete);
    }

    @Test
    void testDeleteRemovesVariablesBlock() {
        configureByText(insideActivityWithPrefixes("variables:\n" +
                "- $<caret>test"));
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        WriteCommandAction.runWriteCommandAction(getProject(), delegateAtCaret::delete);
        String contentAfterDelete = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals(insideActivityWithPrefixes(""), contentAfterDelete);
    }

}
