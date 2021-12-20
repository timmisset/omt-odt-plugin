package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.callable.Callable;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTCommandCallReferenceTest extends OMTTestCase {

    @Test
    void testODTReferenceCanResolveToDefineCommandInsideScript() {
        String content = insideActivityWithPrefixes(
                "onStart: |\n" +
                        "   DEFINE COMMAND commandA => { @LOG('test'); }\n" +
                        "   @co<caret>mmandA();\n" +
                        ""
        );
        configureByText(content);
        // is resolved to the DEFINE statement
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getElementAtCaret() instanceof Callable));
    }

    @Test
    void testODTReferenceCanResolveToDefineCommandInsideActivity() {
        String content = insideActivityWithPrefixes(
                "commands: |\n" +
                        "   DEFINE COMMAND commandA => { @LOG('test'); }\n" +
                        "\n" +
                        "onStart: |\n" +
                        "   @<caret>commandA();\n" +
                        ""
        );
        configureByText(content);
        // is resolved to the defined variable
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getElementAtCaret() instanceof Callable));
    }

    @Test
    void testODTReferenceCanResolveToModelItem() {
        String content = "model:\n" +
                "   MyActivity: !Activity\n" +
                "       title: MijnActiviteit\n" +
                "   MyOtherActivity: !Activity\n" +
                "       onStart: |\n" +
                "           @<caret>MyActivity();\n" +
                "";
        configureByText(content);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getElementAtCaret() instanceof YAMLKeyValue));
    }

    @Test
    void testRefactorRenameCall() {
        String content = "model:\n" +
                "   MyActivity: !Activity\n" +
                "       title: MijnActiviteit\n" +
                "   MyOtherActivity: !Activity\n" +
                "       onStart: |\n" +
                "           @<caret>MyActivity();\n" +
                "";
        final OMTFile omtFile = configureByText(content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("MyNewActivityName");
            Assertions.assertEquals("model:\n" +
                    "   MyNewActivityName: !Activity\n" +
                    "       title: MijnActiviteit\n" +
                    "   MyOtherActivity: !Activity\n" +
                    "       onStart: |\n" +
                    "           @MyNewActivityName();\n", omtFile.getText());
        });
    }
}
