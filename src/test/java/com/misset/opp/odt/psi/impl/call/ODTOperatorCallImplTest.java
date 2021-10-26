package com.misset.opp.odt.psi.impl.call;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.callable.builtin.operators.BuiltInOperator;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTOperatorCallImplTest extends OMTTestCase {

    @Test
    void testGetCallableBuiltinOperator() {
        String content = insideActivityWithPrefixes("onStart: |\n" +
                "   @LOG(<caret>CURRENT_DATETIME);\n" +
                "");
        configureByText(content);
        ReadAction.run(() -> {
            final ODTOperatorCallImpl commit = myFixture.findElementByText("CURRENT_DATETIME", ODTOperatorCallImpl.class);
            Assertions.assertTrue(commit.getCallable() instanceof BuiltInOperator);
        });
    }
}
