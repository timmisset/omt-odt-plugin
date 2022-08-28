package com.misset.opp.omt.util;

import com.intellij.application.options.CodeStyle;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.openapi.application.ReadAction;
import com.misset.opp.odt.inspection.ODTCodeInspectionUnresolvableReference;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.settings.SettingsState;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.jetbrains.yaml.formatter.YAMLCodeStyleSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class OMTImportUtilTest extends OMTInspectionTestCase {

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
            PsiCallable callable = importedFile.getExportingMembersMap().get("query").iterator().next();
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
            PsiCallable callable = importedFile.getExportingMembersMap().get("query").iterator().next();
            String importPath = OMTImportUtil.getImportPath(importingFile, callable);
            Assertions.assertEquals("../folderB/importedFile.omt", importPath);
        });
    }

    @Test
    void testGetImportPathReturnsShorthandPathForDifferentModules() {
        try (MockedStatic<SettingsState> settingsState = Mockito.mockStatic(SettingsState.class)) {
            SettingsState state = mock(SettingsState.class);
            settingsState.when(() -> SettingsState.getInstance(getProject()))
                    .thenReturn(state);

            doReturn("@B/importedFile.omt").when(state).getShorthandPath(anyString());

            myFixture.addFileToProject("folderA/moduleA.module.omt", "moduleName: ModuleA");
            OMTFile importingFile = (OMTFile) myFixture.addFileToProject("folderA/importingFile.omt", "");
            myFixture.addFileToProject("folderB/moduleB.module.omt", "moduleName: ModuleB");
            OMTFile importedFile = (OMTFile) myFixture.addFileToProject("folderB/importedFile.omt", "queries:\n" +
                    "   DEFINE QUERY query => '';");

            ReadAction.run(() -> {
                PsiCallable callable = importedFile.getExportingMembersMap().get("query").iterator().next();
                String importPath = OMTImportUtil.getImportPath(importingFile, callable);
                Assertions.assertEquals("'@B/importedFile.omt'", importPath);
            });
        }
    }

    @Test
    void testGetImportPathFallsBackToRelativePathIfNoShorthandAvailable() {
        try (MockedStatic<SettingsState> settingsState = Mockito.mockStatic(SettingsState.class)) {
            SettingsState state = mock(SettingsState.class);
            settingsState.when(() -> SettingsState.getInstance(getProject()))
                    .thenReturn(state);

            doReturn(null).when(state).getShorthandPath(anyString());

            myFixture.addFileToProject("folderA/moduleA.module.omt", "moduleName: ModuleA");
            OMTFile importingFile = (OMTFile) myFixture.addFileToProject("folderA/importingFile.omt", "");
            myFixture.addFileToProject("folderB/moduleB.module.omt", "moduleName: ModuleB");
            OMTFile importedFile = (OMTFile) myFixture.addFileToProject("folderB/importedFile.omt", "queries:\n" +
                    "   DEFINE QUERY query => '';");

            ReadAction.run(() -> {
                PsiCallable callable = importedFile.getExportingMembersMap().get("query").iterator().next();
                String importPath = OMTImportUtil.getImportPath(importingFile, callable);
                Assertions.assertEquals("../folderB/importedFile.omt", importPath);
            });

        }

    }


    @Test
    void testGetImportIntentionsFromInjected() {
        myFixture.addFileToProject("moduleA.module.omt", "moduleName: ModuleA");
        OMTFile importedFile = (OMTFile) myFixture.addFileToProject("importedFile.omt", "queries:\n" +
                "   DEFINE QUERY queryB => '';");
        OMTFile importingFile = configureByText("importingFile.omt", "queries:\n" +
                "   DEFINE QUERY queryA => <caret>queryB;");


        ReadAction.run(() -> {
            PsiCall call = (PsiCall) myFixture.getElementAtCaret();
            List<LocalQuickFix> quickFixes = OMTImportUtil.getImportQuickFixes(importingFile, call);
            Assertions.assertEquals(1, quickFixes.size());
            Assertions.assertEquals("Import as DEFINE QUERY from ./importedFile.omt", quickFixes.get(0).getName());
        });
    }

    @Test
    void testGetImportIntentionsFromOMT() {
        myFixture.addFileToProject("moduleA.module.omt", "moduleName: ModuleA");
        OMTFile importedFile = (OMTFile) myFixture.addFileToProject("importedFile.omt", "model:\n" +
                "   queryB: !StandaloneQuery\n" +
                "       query: ''\n" +
                "");
        OMTFile importingFile = configureByText("importingFile.omt", "queries:\n" +
                "   DEFINE QUERY queryA => <caret>queryB;");


        ReadAction.run(() -> {
            PsiCall call = (PsiCall) myFixture.getElementAtCaret();
            List<LocalQuickFix> quickFixes = OMTImportUtil.getImportQuickFixes(importingFile, call);
            Assertions.assertEquals(1, quickFixes.size());
            Assertions.assertEquals("Import as StandaloneQuery from ./importedFile.omt", quickFixes.get(0).getName());
        });
    }

    @Test
    void testAddsImportToImportingFile() {
        myFixture.addFileToProject("moduleA.module.omt", "moduleName: ModuleA");
        myFixture.addFileToProject("importedFile.omt", "queries:\n" +
                "   DEFINE QUERY queryB => '';");
        OMTFile importingFile = configureByText("importingFile.omt", "queries:\n" +
                "   DEFINE QUERY queryA => queryB;");

        setYamlIndentation(importingFile);
        invokeQuickFixIntention("Import as DEFINE QUERY from ./importedFile.omt");

        ReadAction.run(() -> Assertions.assertEquals("import:\n" +
                "  ./importedFile.omt:\n" +
                "  - queryB\n" +
                "queries:\n" +
                "   DEFINE QUERY queryA => queryB;", importingFile.getText()));
    }

    @Test
    void testAddsImportToExistingImportedFile() {

        myFixture.addFileToProject("moduleA.module.omt", "moduleName: ModuleA");
        myFixture.addFileToProject("importedFile.omt", "queries:\n" +
                "   DEFINE QUERY queryB => '';\n");
        OMTFile importingFile = configureByText("importingFile.omt", "import:\n" +
                "  ./importedFile.omt:\n" +
                "    - fakeImport\n" +
                "queries:\n" +
                "   DEFINE QUERY queryA => queryB;");

        setYamlIndentation(importingFile);
        invokeQuickFixIntention("Import as DEFINE QUERY from ./importedFile.omt");

        ReadAction.run(() -> Assertions.assertEquals("import:\n" +
                "  ./importedFile.omt:\n" +
                "  - fakeImport\n" +
                "  - queryB\n" +
                "queries:\n" +
                "   DEFINE QUERY queryA => queryB;", importingFile.getText()));
    }

    @Test
    void testAddsNewImportToExistingImportBlock() {
        myFixture.addFileToProject("moduleA.module.omt", "moduleName: ModuleA");
        myFixture.addFileToProject("importedFile.omt", "queries:\n" +
                "   DEFINE QUERY queryB => '';\n");
        OMTFile importingFile = configureByText("importingFile.omt", "import:\n" +
                "  ./fakeFile.omt:\n" +
                "  - fakeImport\n" +
                "queries:\n" +
                "   DEFINE QUERY queryA => queryB;");

        setYamlIndentation(importingFile);
        invokeQuickFixIntention("Import as DEFINE QUERY from ./importedFile.omt");

        ReadAction.run(() -> Assertions.assertEquals("import:\n" +
                "  ./fakeFile.omt:\n" +
                "  - fakeImport\n" +
                "  ./importedFile.omt:\n" +
                "  - queryB\n" +
                "queries:\n" +
                "   DEFINE QUERY queryA => queryB;", importingFile.getText()));
    }


    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTCodeInspectionUnresolvableReference.class);
    }
}
