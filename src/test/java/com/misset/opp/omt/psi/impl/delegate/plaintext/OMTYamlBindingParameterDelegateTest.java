package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTYamlBindingParameterDelegateTest extends OMTTestCase {

    @Test
    void testIsParameter() {
        OMTYamlDelegate delegate = getDelegate();
        Assertions.assertTrue(delegate instanceof OMTYamlBindingParameterDelegate);
        Assertions.assertTrue(((OMTYamlBindingParameterDelegate) delegate).isParameter());
    }

    @Test
    void testDelete() {
        OMTYamlDelegate delegate = getDelegate();
        String before = ReadAction.compute(getFile()::getText);
        WriteCommandAction.runWriteCommandAction(getProject(), delegate::delete);
        String after = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals(before, after);
    }

    @Test
    void testIsUnused() {
        OMTYamlDelegate delegate = getDelegate();
        Assertions.assertTrue(delegate instanceof OMTYamlBindingParameterDelegate);
        Assertions.assertFalse(((OMTYamlBindingParameterDelegate) delegate).isUnused());
    }

    private OMTYamlDelegate getDelegate() {
        String content = "model:\n" +
                "   Component: !Component\n" +
                "       bindings:\n" +
                "           test: $<caret>test";
        configureByText(content);
        return getDelegateAtCaret();
    }
}
