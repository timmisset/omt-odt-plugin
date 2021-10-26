package com.misset.opp.odt.psi.impl.call;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.callable.builtIn.commands.BuiltInCommand;
import com.misset.opp.callable.local.LocalCommand;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTCommandCallImplTest extends OMTTestCase {

    @Test
    void testGetCallableLocalCommand() {
        String content = insideActivityWithPrefixes("onStart: |\n" +
                "   @<caret>COMMIT();\n" +
                "");
        configureByText(content);
        ReadAction.run(() -> {
            final ODTCommandCallImpl commit = myFixture.findElementByText("@COMMIT", ODTCommandCallImpl.class);
            Assertions.assertTrue(commit.getCallable() instanceof LocalCommand);
        });
    }

    @Test
    void testGetCallableBuiltinCommand() {
        String content = insideActivityWithPrefixes("onStart: |\n" +
                "   @<caret>LOG('hello world');\n" +
                "");
        configureByText(content);
        ReadAction.run(() -> {
            final ODTCommandCallImpl commit = myFixture.findElementByText("@LOG", ODTCommandCallImpl.class);
            Assertions.assertTrue(commit.getCallable() instanceof BuiltInCommand);
        });
    }

}
