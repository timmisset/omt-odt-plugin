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
                "commands: |\n" +
                "   DEFINE COMMAND commandA => { }\n" +
                "model:\n" +
                "   ModelItem: !Activity\n" +
                "       title: ModelItem\n" +
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
    void testNoWarningForUsedCommandImport() {
        String content = "import:\n" +
                "   ./importing.omt:\n" +
                "   - commandA\n" +
                "\n" +
                "commands: |\n" +
                "   DEFINE COMMAND command => { @commandA(); }\n" +
                "";
        configureByText(content);
        assertNoWarning("Import for commandA is never used");
    }

    @Test
    void testNoWarningForUsedModelItemImport() {
        String content = "import:\n" +
                "   ./importing.omt:\n" +
                "   - ModelItem\n" +
                "\n" +
                "commands: |\n" +
                "   DEFINE COMMAND command => { @ModelItem(); }\n" +
                "";
        configureByText(content);
        assertNoWarning("Import for ModelItem is never used");
    }

    @Test
    void testNoWarningForDeferredImport() {
        String content = "import:\n" +
                "   ./importing.omt:\n" +
                "   - memberA";
        configureByText("index.omt", content);

        addFileToProject("deferredImport.omt", "" +
                "import: \n" +
                "   ./index.omt:\n" +
                "   - memberA");

        assertNoWarning("Import for memberA is never used");
    }

    @Test
    void testNoWarningForExportedImport() {
        String content = "import:\n" +
                "   ./importing.omt:\n" +
                "   - memberA\n" +
                "export:\n" +
                "- memberA";
        configureByText("myModule.module.omt", content);
        assertNoWarning("Import for memberA is never used");
    }

    @Test
    void testNoWarningForReferencedUsageWithUnresolvableImport() {
        String content = "import:\n" +
                "    ./myfile.omt:\n" +
                "    - myQuery\n" +
                "\n" +
                "model:\n" +
                "    MyActivity: !Activity\n" +
                "        payload:\n" +
                "            myQuery:\n" +
                "                query: myQuery\n";
        configureByText(content);
        assertNoWarning("Import for myQuery is never used");
    }

    @Test
    void testNoWarningForReferencedPayloadUsageWithResolvableImport() {
        addFileToProject("myfile.omt", "queries: |\n" +
                "   DEFINE QUERY myQuery => '';\n");
        String content = "import:\n" +
                "    ./myfile.omt:\n" +
                "    - myQuery\n" +
                "\n" +
                "model:\n" +
                "    MyActivity: !Activity\n" +
                "        payload:\n" +
                "            myQuery:\n" +
                "                query: myQuery\n";
        configureByText(content);
        assertNoWarning("Import for myQuery is never used");
    }

    @Test
    void testNoWarningForReferencedRuleUsageWithResolvableImport() {
        addFileToProject("myfile.omt", "queries: |\n" +
                "   DEFINE QUERY myQuery => '';\n");
        String content = "import:\n" +
                "    ./myfile.omt:\n" +
                "    - myQuery\n" +
                "\n" +
                "model:\n" +
                "    MyActivity: !Activity\n" +
                "        rules:\n" +
                "            myRule: myQuery\n";
        configureByText(content);
        assertNoWarning("Import for myQuery is never used");
    }

    @Test
    void testNoWarningForUsedProcedure() {
        String content = "import:\n" +
                "   myfile.omt:\n" +
                "   - myProcedure\n" +
                "\n" +
                "procedures:\n" +
                "- myProcedure\n";
        configureByText("module.omt", content);
        assertNoWarning("Import for myProcedure is never used");
    }
}
