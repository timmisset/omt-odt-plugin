package com.misset.opp.omt.psi.impl.delegate.keyvalue;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTYamlModelItemDelegateTest extends OMTTestCase {

    @Test
    void testIsUnusedIsTrueForNonCallableModelItems() {
        String content = "model:\n" +
                "   <caret>Component: !Component\n" +
                "       onInit:\n";
        configureByText(content);
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        Assertions.assertTrue(delegateAtCaret instanceof OMTYamlModelItemDelegate);
        Assertions.assertTrue(((OMTYamlModelItemDelegate) delegateAtCaret).isUnused());
    }

    @Test
    void testSafeDeleteModelItem() {
        String content = "model:\n" +
                "   <caret>Component: !Component\n" +
                "       onInit:\n";
        configureByText(content);
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        WriteCommandAction.runWriteCommandAction(getProject(), delegateAtCaret::delete);
        String contentAfterDelete = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals("model:\n" +
                "   ", contentAfterDelete);
    }

    @Test
    void testIsUnusedIsTrueForUnusedCallableModelItems() {
        String content = "model:\n" +
                "   Activity: !Activity\n" +
                "       onStart: @LOG('test');\n" +
                "   <caret>Procedure: !Procedure\n" +
                "       onRun: @Activity();\n";
        configureByText(content);
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        Assertions.assertTrue(delegateAtCaret instanceof OMTYamlModelItemDelegate);
        underProgress(() -> ReadAction.run(() -> Assertions.assertTrue(((OMTYamlModelItemDelegate) delegateAtCaret).isUnused())));
    }

    @Test
    void testIsUnusedIsFalseForUsedCallableModelItems() {
        String content = "model:\n" +
                "   <caret>Activity: !Activity\n" +
                "       onStart: @LOG('test');\n" +
                "   Procedure: !Procedure\n" +
                "       onRun: @Activity();\n";
        configureByText(content);
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        Assertions.assertTrue(delegateAtCaret instanceof OMTYamlModelItemDelegate);
        underProgress(() -> ReadAction.run(() -> Assertions.assertFalse(((OMTYamlModelItemDelegate) delegateAtCaret).isUnused())));
    }
}
