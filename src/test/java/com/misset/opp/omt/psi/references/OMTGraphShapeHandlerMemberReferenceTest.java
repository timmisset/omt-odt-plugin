package com.misset.opp.omt.psi.references;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTGraphShapeHandlerMemberReferenceTest extends OMTTestCase {

    @Test
    void testHasReference() {
        String content = "model:\n" +
                "   MyShapeHandler: !GraphShapeHandlers\n" +
                "       shape: /my:shape\n" +
                "" +
                "handlers:\n" +
                "   - <caret>MyShapeHandler";
        configureByText("my.module.omt", content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof YAMLKeyValue);
        });
    }

    @Test
    void testRefactorRenames() {
        String content = "model:\n" +
                "   <caret>MyShapeHandler: !GraphShapeHandlers\n" +
                "       shape: /my:shape\n" +
                "" +
                "handlers:\n" +
                "   - MyShapeHandler";
        configureByText("my.module.omt", content);
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("MyNewShapeHandler");
            Assertions.assertEquals("model:\n" +
                    "   MyNewShapeHandler: !GraphShapeHandlers\n" +
                    "       shape: /my:shape\n" +
                    "handlers:\n" +
                    "   - MyNewShapeHandler", myFixture.getFile().getText());
        });
    }

    @Test
    void testHasReferenceFromImported() {
        String content = "model:\n" +
                "   MyShapeHandler: !GraphShapeHandlers\n" +
                "       shape: /my:shape\n";
        addFileToProject("./myHandler.omt", content);

        content = "import:\n" +
                "   ./myHandler.omt:\n" +
                "   - MyShapeHandler\n" +
                "handlers:\n" +
                "   - <caret>MyShapeHandler";
        configureByText("my.module.omt", content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof YAMLKeyValue);
        });
    }

}
