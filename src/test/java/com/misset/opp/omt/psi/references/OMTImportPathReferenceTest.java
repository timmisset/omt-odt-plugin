package com.misset.opp.omt.psi.references;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFilesOrDirectoriesProcessor;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class OMTImportPathReferenceTest extends OMTTestCase {

    @Test
    void testImportReferenceHasReferenceToImportedFile() {
        configureByText("importedFile.omt", "" +
                "queries:\n" +
                "   DEFINE QUERY query => '';");
        configureByText("" +
                "import:\n" +
                "   ./import<caret>edFile.omt:\n" +
                "   - query\n" +
                "");

        ReadAction.run(() -> Assertions.assertTrue(myFixture.getElementAtCaret() instanceof OMTFile));
    }

    @Test
    void testImportReferenceBindsToReplacedFile() throws IOException {
        final OMTFile importedFile = configureByText("importedFile.omt", "" +
                "queries:\n" +
                "   DEFINE QUERY query => '';");
        configureByText("" +
                "import:\n" +
                "   ./import<caret>edFile.omt:\n" +
                "   - query\n" +
                "");

        WriteAction.runAndWait(() -> {
            final VirtualFile subfolder = importedFile.getVirtualFile().getParent().createChildDirectory(this, "subfolder");
            final PsiDirectory to = PsiManager.getInstance(getProject()).findDirectory(subfolder);
            new MoveFilesOrDirectoriesProcessor(getProject(), new PsiElement[]{importedFile}, to, false, true, null, null).run();
            assertTrue(getEditor().getDocument().getText().contains("./subfolder/importedFile.omt"));
        });
    }
}
