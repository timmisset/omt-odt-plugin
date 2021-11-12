package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.callable.builtin.commands.BuiltInCommand;
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
        ReadAction.run(() -> Assertions.assertTrue(getCallByName("COMMIT").getCallable() instanceof LocalCommand));
    }

    @Test
    void testGetCallableBuiltinCommand() {
        String content = insideActivityWithPrefixes("onStart: |\n" +
                "   @<caret>LOG('hello world');\n" +
                "");
        configureByText(content);
        ReadAction.run(() -> Assertions.assertTrue(getCallByName("LOG").getCallable() instanceof BuiltInCommand));
    }

}
