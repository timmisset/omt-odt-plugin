package com.misset.opp.omt.inspection.unused;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTUnusedImportMemberInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnusedImportMemberInspection.class);
    }

    @BeforeEach
    protected void setUp() {
        super.setUp();
        addFileToProject("importing.omt", "" +
                "queries: |\n" +
                "   DEFINE QUERY memberA => 'memberA';\n" +
                "");
    }

    @Test
    void testWarningForUnusedImport() {
        String content = "import:\n" +
                "   ./importing.omt:\n" +
                "   - memberA";
        configureByText(content);
        assertHasWarning("Import for memberA is never used");
    }

    @Test
    void testRemoveUnusedImportRemovesOnlyImportAndImportPath() {
        String content = "import:\n" +
                "   ./importing.omt:\n" +
                "   - memberA";
        configureByText(content);
        IntentionAction remove_prefix = getQuickFixIntention("Remove import member");
        WriteCommandAction.runWriteCommandAction(getProject(), () -> remove_prefix.invoke(getProject(), getEditor(), getFile()));
        String contentAfterRemoval = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals("import:\n" +
                "   ", contentAfterRemoval);
    }

    @Test
    void testRemoveUnusedImportRemovesOnlyImport() {
        String content = "import:\n" +
                "   ./importing.omt:\n" +
                "   - memberA\n" +
                "   - memberB\n";
        configureByText(content);
        IntentionAction remove_prefix = getQuickFixIntention("Remove import member");
        WriteCommandAction.runWriteCommandAction(getProject(), () -> remove_prefix.invoke(getProject(), getEditor(), getFile()));
        String contentAfterRemoval = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals("import:\n" +
                "   ./importing.omt:\n" +
                "     - memberB\n", contentAfterRemoval);
    }

    @Test
    void testNoWarningForUsedImport() {
        String content = "import:\n" +
                "   ./importing.omt:\n" +
                "   - memberA\n" +
                "\n" +
                "queries: \n" +
                "   DEFINE QUERY query => memberA;\n" +
                "";
        configureByText(content);
        assertNoWarning("Import for memberA is never used");
    }

    @Test
    void testNoWarningForDeferredImport() {
        addFileToProject("deferredImport.omt", "" +
                "import: \n" +
                "   ./index.omt:\n" +
                "   - memberA");
        String content = "import:\n" +
                "   ./importing.omt:\n" +
                "   - memberA";
        configureByText("index.omt", content);
        assertNoWarning("Import for memberA is never used");
    }

    @Test
    void testWarningForNonUsedDeferredImport() {
        // the memberA is not used from the index.omt import
        // therefore, the import of memberA in index.omt is still unused
        addFileToProject("deferredImport.omt", "" +
                "import: \n" +
                "   ./index.omt:\n" +
                "   - memberB\n" +
                "   ./importing.omt:\n" +
                "   - memberA\n");
        String content = "import:\n" +
                "   ./importing.omt:\n" +
                "   - memberA";
        configureByText("index.omt", content);
        assertHasWarning("Import for memberA is never used");
    }
}
