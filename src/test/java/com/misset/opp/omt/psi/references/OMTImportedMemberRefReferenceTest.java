package com.misset.opp.omt.psi.references;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.testCase.OMTCompletionTestCase;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTImportedMemberRefReferenceTest extends OMTCompletionTestCase {
    @Test
    void testHasReference() {
        String content = "model:\n" +
                "   MyActivity: !Activity\n" +
                "       title: Hello world\n" +
                "" +
                "export:\n" +
                "   - <caret>MyActivity";
        configureByText("my.module.omt", content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof YAMLKeyValue);
        });
    }

    @Test
    void testRefactorRenames() {
        String content = "model:\n" +
                "   MyActivity: !Activity\n" +
                "       title: Hello world\n" +
                "" +
                "export:\n" +
                "   - <caret>MyActivity";
        configureByText("my.module.omt", content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("MyNewActivity");
            Assertions.assertEquals("model:\n" +
                    "   MyNewActivity: !Activity\n" +
                    "       title: Hello world\n" +
                    "export:\n" +
                    "   - MyNewActivity", myFixture.getFile().getText());
        });
    }

    @Test
    void testHasReferenceFromImported() {
        String content = "model:\n" +
                "   MyActivity: !Activity\n" +
                "       title: Hello world\n";
        addFileToProject("./myActivity.omt", content);

        content = "import:\n" +
                "   ./myActivity.omt:\n" +
                "   - MyActivity\n" +
                "export:\n" +
                "   - <caret>MyActivity";
        configureByText("my.module.omt", content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof YAMLKeyValue);
        });
    }
}
