package com.misset.opp.omt.it;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Test;

/**
 * Scenarios where imported members from other OMT files are shadowed by ODT declarations
 */
class ImportedMemberShadowingIT extends OMTTestCase {

    @Test
    void testResolvesToImportedMember() {
        addFileToProject("importedFile.omt",
                "commands:\n" +
                        "   DEFINE COMMAND myMember => { }");
        String content = "" +
                "import:\n" +
                "   importedFile.omt:\n" +
                "   - myMember\n" +
                "\n" +
                "commands: |\n" +
                "   DEFINE COMMAND myMember => { @<caret>myMember(); }";
        configureByText(content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            assertEquals("importedFile.omt", elementAtCaret.getContainingFile().getName());
        });
    }

    @Test
    void testResolvesToShadowedMember() {
        addFileToProject("importedFile.omt",
                "commands:\n" +
                        "   DEFINE COMMAND myMember => { }");
        String content = "" +
                "import:\n" +
                "   importedFile.omt:\n" +
                "   - myMember\n" +
                "\n" +
                "commands: |\n" +
                "   DEFINE COMMAND myMember => { @myMember(); }\n" +
                "   DEFINE COMMAND anotherMember => { @<caret>myMember(); }";
        configureByText("file.omt", content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            assertEquals("file.omt", elementAtCaret.getContainingFile().getName());
        });
    }

    @Test
    void testResolvesToShadowedMemberFromOtherSection() {
        addFileToProject("importedFile.omt",
                "commands:\n" +
                        "   DEFINE COMMAND myMember => { }");
        String content = "" +
                "import:\n" +
                "   importedFile.omt:\n" +
                "   - myMember\n" +
                "\n" +
                "commands: |\n" +
                "   DEFINE COMMAND myMember => { @myMember(); }\n" +
                "   DEFINE COMMAND anotherMember => { @myMember(); }\n" +
                "\n" +
                "model:\n" +
                "   MyActivity: !Activity\n" +
                "       onStart: |\n" +
                "           @<caret>myMember();";
        configureByText("file.omt", content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            assertEquals("file.omt", elementAtCaret.getContainingFile().getName());
        });
    }

    @Test
    void testResolvesToShadowedMemberFromNestedSection() {
        String content = "commands: |\n" +
                "   DEFINE COMMAND myMember => { }\n" +
                "\n" +
                "model:\n" +
                "   MyActivity: !Activity\n" +
                "       commands: |\n" +
                "           DEFINE COMMAND myMember => { }\n" +
                "       onStart: |\n" +
                "           @<caret>myMember();";
        configureByText("file.omt", content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            assertEquals(96, InjectedLanguageManager.getInstance(getProject())
                    .getInjectionHost(elementAtCaret)
                    .getTextOffset());
        });
    }

    @Test
    void testResolvesToRootMemberFromUnaccessibleSection() {
        String content = "commands: |\n" +
                "   DEFINE COMMAND myMember => { }\n" +
                "\n" +
                "model:\n" +
                "   MyActivity: !Activity\n" +
                "       commands: |\n" +
                "           DEFINE COMMAND myMember => { }\n" +
                "       onStart: |\n" +
                "           @myMember();\n" +
                "   MyProcedure: !Procedure\n" +
                "       onRun: |\n" +
                "           @<caret>myMember();\n";
        configureByText("file.omt", content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            assertEquals(10, InjectedLanguageManager.getInstance(getProject())
                    .getInjectionHost(elementAtCaret)
                    .getTextOffset());
        });
    }

}
