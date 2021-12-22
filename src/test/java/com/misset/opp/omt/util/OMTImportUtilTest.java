package com.misset.opp.omt.util;

import com.intellij.application.options.CodeStyle;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.omt.indexing.OMTExportedMembersIndex;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.settings.SettingsState;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.formatter.YAMLCodeStyleSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class OMTImportUtilTest extends OMTTestCase {

    private void setYamlIndentation(OMTFile file) {
        YAMLCodeStyleSettings yamlCodeStyleSettings = CodeStyle.getCustomSettings(file, YAMLCodeStyleSettings.class);
        yamlCodeStyleSettings.INDENT_SEQUENCE_VALUE = false;
    }

    @Test
    void testGetImportPathReturnsRelativePathForSameModule() {
        myFixture.addFileToProject("moduleA.module.omt", "moduleName: ModuleA");
        OMTFile importingFile = (OMTFile) myFixture.addFileToProject("importingFile.omt", "");
        OMTFile importedFile = (OMTFile) myFixture.addFileToProject("importedFile.omt", "queries:\n" +
                "   DEFINE QUERY query => '';");

        ReadAction.run(() -> {
            PsiCallable callable = importedFile.getExportingMembersMap().get("query").get(0);
            String importPath = OMTImportUtil.getImportPath(importingFile, callable);
            Assertions.assertEquals("./importedFile.omt", importPath);
        });
    }

    @Test
    void testGetImportPathReturnsRelativePathForSameModuleFromDifferentFolders() {
        myFixture.addFileToProject("moduleA.module.omt", "moduleName: ModuleA");
        OMTFile importingFile = (OMTFile) myFixture.addFileToProject("folderA/importingFile.omt", "");
        OMTFile importedFile = (OMTFile) myFixture.addFileToProject("folderB/importedFile.omt", "queries:\n" +
                "   DEFINE QUERY query => '';");

        ReadAction.run(() -> {
            PsiCallable callable = importedFile.getExportingMembersMap().get("query").get(0);
            String importPath = OMTImportUtil.getImportPath(importingFile, callable);
            Assertions.assertEquals("../folderB/importedFile.omt", importPath);
        });
    }

    @Test
    void testGetImportPathReturnsShorthandPathForDifferentModules() {
        Map<String, String> mappingPaths = SettingsState.getInstance(getProject()).mappingPaths;
        mappingPaths.put("@A", "folderA");
        mappingPaths.put("@B", "folderB");

        myFixture.addFileToProject("folderA/moduleA.module.omt", "moduleName: ModuleA");
        OMTFile importingFile = (OMTFile) myFixture.addFileToProject("folderA/importingFile.omt", "");
        myFixture.addFileToProject("folderB/moduleB.module.omt", "moduleName: ModuleB");
        OMTFile importedFile = (OMTFile) myFixture.addFileToProject("folderB/importedFile.omt", "queries:\n" +
                "   DEFINE QUERY query => '';");

        ReadAction.run(() -> {
            PsiCallable callable = importedFile.getExportingMembersMap().get("query").get(0);
            String importPath = OMTImportUtil.getImportPath(importingFile, callable);
            Assertions.assertEquals("'@B/importedFile.omt'", importPath);
        });
    }

    @Test
    void testGetImportPathFallsBackToRelativePathIfNoShorthandAvailable() {

        myFixture.addFileToProject("folderA/moduleA.module.omt", "moduleName: ModuleA");
        OMTFile importingFile = (OMTFile) myFixture.addFileToProject("folderA/importingFile.omt", "");
        myFixture.addFileToProject("folderB/moduleB.module.omt", "moduleName: ModuleB");
        OMTFile importedFile = (OMTFile) myFixture.addFileToProject("folderB/importedFile.omt", "queries:\n" +
                "   DEFINE QUERY query => '';");

        ReadAction.run(() -> {
            PsiCallable callable = importedFile.getExportingMembersMap().get("query").get(0);
            String importPath = OMTImportUtil.getImportPath(importingFile, callable);
            Assertions.assertEquals("'@B/importedFile.omt'", importPath);
        });
    }

    @Test
    void testGetImportIntentions() {
        myFixture.addFileToProject("moduleA.module.omt", "moduleName: ModuleA");
        OMTFile importedFile = (OMTFile) myFixture.addFileToProject("importedFile.omt", "queries:\n" +
                "   DEFINE QUERY queryB => '';");
        OMTExportedMembersIndex.analyse(importedFile);
        OMTFile importingFile = configureByText("importingFile.omt", "queries:\n" +
                "   DEFINE QUERY queryA => <caret>queryB;");


        ReadAction.run(() -> {
            PsiCall call = (PsiCall) myFixture.getElementAtCaret();
            IntentionAction[] importIntentions = OMTImportUtil.getImportIntentions(importingFile, call);
            Assertions.assertEquals(1, importIntentions.length);
            Assertions.assertEquals("Import as DEFINE QUERY from ./importedFile.omt", importIntentions[0].getText());
        });
    }

    @Test
    void testAddsImportToImportingFile() {
        myFixture.addFileToProject("moduleA.module.omt", "moduleName: ModuleA");
        OMTFile importedFile = (OMTFile) myFixture.addFileToProject("importedFile.omt", "queries:\n" +
                "   DEFINE QUERY queryB => '';");
        OMTExportedMembersIndex.analyse(importedFile);
        OMTFile importingFile = configureByText("importingFile.omt", "queries:\n" +
                "   DEFINE QUERY queryA => <caret>queryB;");

        setYamlIndentation(importedFile);

        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            PsiCall call = (PsiCall) myFixture.getElementAtCaret();
            IntentionAction[] importIntentions = OMTImportUtil.getImportIntentions(importingFile, call);
            Assertions.assertEquals(1, importIntentions.length);
            Assertions.assertEquals("Import as DEFINE QUERY from ./importedFile.omt", importIntentions[0].getText());
            importIntentions[0].invoke(getProject(), getEditor(), importingFile);
            Assertions.assertEquals("import:\n" +
                    "  ./importedFile.omt:\n" +
                    "  - queryB\n" +
                    "queries:\n" +
                    "   DEFINE QUERY queryA => queryB;", importingFile.getText());
        });
    }

    @Test
    void testAddsImportToExistingImportedFile() {

        myFixture.addFileToProject("moduleA.module.omt", "moduleName: ModuleA");
        OMTFile importedFile = (OMTFile) myFixture.addFileToProject("importedFile.omt", "queries:\n" +
                "   DEFINE QUERY queryB => '';\n");
        OMTExportedMembersIndex.analyse(importedFile);
        OMTFile importingFile = configureByText("importingFile.omt", "import:\n" +
                "  ./importedFile.omt:\n" +
                "    - fakeImport\n" +
                "queries:\n" +
                "   DEFINE QUERY queryA => <caret>queryB;");

        setYamlIndentation(importedFile);

        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            PsiCall call = (PsiCall) myFixture.getElementAtCaret();
            IntentionAction[] importIntentions = OMTImportUtil.getImportIntentions(importingFile, call);
            Assertions.assertEquals(1, importIntentions.length);
            Assertions.assertEquals("Import as DEFINE QUERY from ./importedFile.omt", importIntentions[0].getText());
            importIntentions[0].invoke(getProject(), getEditor(), importingFile);
            Assertions.assertEquals("import:\n" +
                    "  ./importedFile.omt:\n" +
                    "  - fakeImport\n" +
                    "  - queryB\n" +
                    "queries:\n" +
                    "   DEFINE QUERY queryA => queryB;", importingFile.getText());
        });
    }

    @Test
    void testAddsNewImportToExistingImportBlock() {
        myFixture.addFileToProject("moduleA.module.omt", "moduleName: ModuleA");
        OMTFile importedFile = (OMTFile) myFixture.addFileToProject("importedFile.omt", "queries:\n" +
                "   DEFINE QUERY queryB => '';\n");
        OMTExportedMembersIndex.analyse(importedFile);
        OMTFile importingFile = configureByText("importingFile.omt", "import:\n" +
                "  ./fakeFile.omt:\n" +
                "    - fakeImport\n" +
                "queries:\n" +
                "   DEFINE QUERY queryA => <caret>queryB;");

        setYamlIndentation(importedFile);

        WriteCommandAction.runWriteCommandAction(getProject(), () -> {
            PsiCall call = (PsiCall) myFixture.getElementAtCaret();
            IntentionAction[] importIntentions = OMTImportUtil.getImportIntentions(importingFile, call);
            Assertions.assertEquals(1, importIntentions.length);
            Assertions.assertEquals("Import as DEFINE QUERY from ./importedFile.omt", importIntentions[0].getText());
            importIntentions[0].invoke(getProject(), getEditor(), importingFile);
            Assertions.assertEquals("import:\n" +
                    "  ./fakeFile.omt:\n" +
                    "  - fakeImport\n" +
                    "  ./importedFile.omt:\n" +
                    "  - queryB\n" +
                    "queries:\n" +
                    "   DEFINE QUERY queryA => queryB;", importingFile.getText());
        });
    }


}