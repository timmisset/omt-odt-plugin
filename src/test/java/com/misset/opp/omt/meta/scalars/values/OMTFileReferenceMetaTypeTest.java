package com.misset.opp.omt.meta.scalars.values;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFilesOrDirectoriesProcessor;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

class OMTFileReferenceMetaTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTValueInspection.class);
    }

    @Test
    void testHasErrorWhenNotEndingWithJsonExtension() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       path: test.txt";
        configureByText(content);
        assertHasError(".json file required");
    }

    @Test
    void testHasNoWhenEndingWithJsonExtension() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       path: test.json";
        configureByText(content);
        assertNoError(".json file required");
    }

    @Test
    void testHasErrorWhenFileCannotBeFound() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       path: test.json";
        configureByText(content);
        assertHasError("Cannot find file");
    }

    @Test
    void testHasNoErrorWhenFileCanBeFound() {
        myFixture.addFileToProject("test.json", "");
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       path: test.json";
        configureByText(content);
        assertNoError("Cannot find file");
    }

    @Test
    void testRebindsToFileAfterMove() throws IOException {
        PsiFile importedFile = myFixture.addFileToProject("test.json", "");
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       path: test.json";
        configureByText(content);

        WriteAction.runAndWait(() -> {
            final VirtualFile subfolder = importedFile.getVirtualFile().getParent().createChildDirectory(this, "subfolder");
            final PsiDirectory to = PsiManager.getInstance(getProject()).findDirectory(subfolder);
            new MoveFilesOrDirectoriesProcessor(getProject(), new PsiElement[]{importedFile}, to, false, true, null, null).run();
            assertTrue(getEditor().getDocument().getText().contains("./subfolder/test.json"));
        });
    }
}
