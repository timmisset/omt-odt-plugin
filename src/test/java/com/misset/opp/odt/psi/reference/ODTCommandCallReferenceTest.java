package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.resolvable.Callable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTCommandCallReferenceTest extends ODTTestCase {

    @Test
    void testODTReferenceCanResolveToDefineCommandInsideScript() {
        String content = "DEFINE COMMAND commandA => { @LOG('test'); }\n" +
                "@co<caret>mmandA()";
        configureByText(content);
        // is resolved to the DEFINE statement
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getElementAtCaret() instanceof Callable));
    }

    @Test
    void testODTReferenceCannotResolveToDefineCommandInsideScriptBelowCurrentCommand() {
        String content = "DEFINE COMMAND commandA => { @co<caret>mmandB(); }\n" +
                "DEFINE COMMAND commandB => { @LOG('test'); }";
        configureByText(content);
        // is resolved to the DEFINE statement
        ReadAction.run(() -> Assertions.assertNull(
                myFixture.findElementByText("@commandB", ODTCall.class).getReference().resolve())
        );
    }

    @Test
    void testODTReferenceCanResolveToExternalCallable() {
        String content = "@<caret>LOG();";
        configureByText(content);
        // is resolved to the defined variable
        ReadAction.run(() -> {
            ODTCall call = myFixture.findElementByText("@LOG()", ODTCall.class);
            Assertions.assertNotNull(call.getCallable());
        });
    }

    @Test
    void testRefactorRenameCall() {
        String content = "" +
                "DEFINE COMMAND name => { }\n" +
                "@<caret>name();";
        ODTFileTestImpl odtFileTest = configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("newName");
            Assertions.assertEquals(
                    "DEFINE COMMAND newName => { }\n" +
                            "@newName();", odtFileTest.getText());
        });
    }
}
