package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTYamlBaseParameterDelegateTest extends OMTTestCase {

    @Test
    void testIsParameter() {
        String content = "model:\n" +
                "   StandaloneQuery: !StandaloneQuery\n" +
                "       base: $b<caret>ase\n" +
                "       query: 'test'";
        configureByText(content);
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        Assertions.assertTrue(delegateAtCaret instanceof OMTYamlBaseParameterDelegate);
        Assertions.assertTrue(((OMTYamlBaseParameterDelegate) delegateAtCaret).isParameter());
    }

    @Test
    void testSafeDelete() {
        String content = "model:\n" +
                "   StandaloneQuery: !StandaloneQuery\n" +
                "       base: $b<caret>ase\n" +
                "       query: 'test'";
        configureByText(content);
        OMTYamlDelegate delegateAtCaret = getDelegateAtCaret();
        WriteCommandAction.runWriteCommandAction(getProject(), delegateAtCaret::delete);
        ReadAction.run(() -> Assertions.assertEquals("model:\n" +
                "   StandaloneQuery: !StandaloneQuery\n" +
                "     \n" +
                "     query: 'test'", getFile().getText()));
    }

}
