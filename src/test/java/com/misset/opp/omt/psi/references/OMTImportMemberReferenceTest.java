package com.misset.opp.omt.psi.references;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageInfo;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class OMTImportMemberReferenceTest extends OMTTestCase {

    @Test
    void testImportReferenceHasReferenceToQuery() {
        configureByText("importedFile.omt", "" +
                "queries:\n" +
                "   DEFINE QUERY query => '';");
        configureByText("" +
                "import:\n" +
                "   ./importedFile.omt:\n" +
                "   - query\n" +
                "");

        ReadAction.run(() -> {
            final YAMLPlainTextImpl query = myFixture.findElementByText("query", YAMLPlainTextImpl.class);
            Assertions.assertTrue(query.getReference().resolve() instanceof PsiCallable);
        });
    }

    @Test
    void testImportReferenceHasReferenceToCommand() {
        configureByText("importedFile.omt", "" +
                "commands:\n" +
                "   DEFINE COMMAND command => { }");
        configureByText("" +
                "import:\n" +
                "   ./importedFile.omt:\n" +
                "   - command\n" +
                "");
        ReadAction.run(() -> {
            final YAMLPlainTextImpl command = myFixture.findElementByText("command", YAMLPlainTextImpl.class);
            Assertions.assertTrue(command.getReference().resolve() instanceof PsiCallable);
        });
    }

    @Test
    void testCanFindUsage() {
        configureByText("" +
                "import:\n" +
                "   ./importedFile.omt:\n" +
                "   - co<caret>mmand\n" +
                "");
        configureByText("importedFile.omt", "" +
                "commands:\n" +
                "   DEFINE COMMAND comm<caret>and => { }");

        underProgress(() -> ReadAction.run(() -> {
            final PsiCallable callable = (PsiCallable) myFixture.getElementAtCaret();
            final Collection<UsageInfo> usages = myFixture.findUsages(callable);
            Assertions.assertEquals(1, usages.size());
            Assertions.assertTrue(usages.stream().findFirst().get().getReference() instanceof OMTImportMemberReference);
        }));
    }

    @Test
    void testRenameCallable() {
        final OMTFile importingFile = configureByText("" +
                "import:\n" +
                "   ./importedFile.omt:\n" +
                "   - command\n" +
                "");
        OMTFile omtFile = configureByText("importedFile.omt", "" +
                "commands:\n" +
                "   DEFINE COMMAND comm<caret>and => { }");
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("commandNewName");
            Assertions.assertEquals("import:\n" +
                    "   ./importedFile.omt:\n" +
                    "   - commandNewName\n", importingFile.getText());
            Assertions.assertEquals("commands:\n" +
                    "   DEFINE COMMAND commandNewName => { }", omtFile.getText());
        });
    }

    @Test
    void testRenameCall() {
        final OMTFile importingFile = configureByText("" +
                "import:\n" +
                "   ./importedFile.omt:\n" +
                "   - Procedure\n" +
                "");
        OMTFile omtFile = configureByText("importedFile.omt", "" +
                "model:\n" +
                "   <caret>Procedure: !Procedure\n" +
                "       onRun: @LOG('hi');\n" +
                "");
        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            myFixture.renameElementAtCaret("ProcedureNewName");
            Assertions.assertEquals("import:\n" +
                    "   ./importedFile.omt:\n" +
                    "   - ProcedureNewName\n", importingFile.getText());
            Assertions.assertEquals("model:\n" +
                    "   ProcedureNewName: !Procedure\n" +
                    "       onRun: @LOG('hi');\n", omtFile.getText());
        });
    }

    @Test
    void testCanReferenceViaIndex() {
        myFixture.addFileToProject("index.omt", "import:\n" +
                "   exportingFile.omt:\n" +
                "   - memberA\n" +
                "   - memberB\n");
        myFixture.addFileToProject("exportingFile.omt", "queries:\n" +
                "   DEFINE QUERY memberA => '';\n" +
                "   DEFINE QUERY memberB => '';\n");
        myFixture.configureByText("importingFile.omt", "import:\n" +
                "   index.omt:\n" +
                "   - <caret>memberA\n" +
                "   - memberB\n");
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof PsiCallable);
        });
    }
}
