package com.misset.opp.omt.psi.impl;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.SearchScope;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.testcase.OMTTestCase;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Map;

class OMTFileImplTest extends OMTTestCase {

    @Test
    void getFileTypeOMT() {

        final PsiFile psiFile = configureByText("test.omt", "model:\n");
        Assertions.assertEquals(OMTFileType.INSTANCE, psiFile.getFileType());
    }


    @Test
    void testGetExportingMembersMapReturnsAllExportableMembers() {
        String content = "model: \n" +
                "   MyActivity: !Activity\n" +
                "       title: Title\n" +
                "       commands: |\n" +
                "           DEFINE COMMAND notExportedCommand => {}\n" +
                "commands: |\n" +
                "   DEFINE COMMAND exportedCommand => {}\n" +
                "queries: |\n" +
                "   DEFINE QUERY exportedQuery => 'hello world';\n" +
                "";
        final OMTFile omtFile = configureByText(content);
        final Map<String, Collection<PsiCallable>> exportingMembersMap = omtFile.getExportingMembersMap();
        assertNotEmpty(exportingMembersMap.keySet());
        Assertions.assertTrue(exportingMembersMap.containsKey("MyActivity"));
        Assertions.assertTrue(exportingMembersMap.containsKey("exportedCommand"));
        Assertions.assertTrue(exportingMembersMap.containsKey("exportedQuery"));
        Assertions.assertFalse(exportingMembersMap.containsKey("notExportedCommand"));
    }

    @Test
    void testGetExportingMembersFromImportedFile() {
        String importedFile = "model: \n" +
                "   MyActivity: !Activity\n" +
                "       title: Title\n" +
                "";
        configureByText("importedFile.omt", importedFile);

        String importingFile = "import:\n" +
                "   ./importedFile.omt:\n" +
                "   - MyActivity\n" +
                "" +
                "queries: |\n" +
                "   /**\n" +
                "     * bla bla bla\n" +
                "     */\n" +
                "   DEFINE QUERY query => 'hello world';\n" +
                "";
        final OMTFile omtFile = configureByText(importingFile);
        final Map<String, Collection<PsiCallable>> exportingMembersMap = omtFile.getExportingMembersMap();
        Assertions.assertTrue(exportingMembersMap.containsKey("MyActivity"));
        Assertions.assertTrue(exportingMembersMap.containsKey("query"));
        Assertions.assertNotNull(exportingMembersMap.get("MyActivity"));
    }

    @Test
    void testGetExportingMembersFromIndexFile() {
        myFixture.addFileToProject("exportingFile.omt", "queries:\n" +
                "   DEFINE QUERY memberA => '';\n" +
                "   DEFINE QUERY memberB => '';\n");
        OMTFile omtFile = configureByText("index.omt", "import:\n" +
                "   exportingFile.omt:\n" +
                "   - memberA\n" +
                "   - memberB\n");

        ReadAction.run(() -> {
            Map<String, Collection<PsiCallable>> declaredExportingMembersMap = omtFile.getDeclaredExportingMembersMap();
            Map<String, Collection<PsiCallable>> exportingMembersMap = omtFile.getExportingMembersMap();
            Assertions.assertTrue(declaredExportingMembersMap.isEmpty());
            Assertions.assertFalse(exportingMembersMap.isEmpty());
            assertContainsElements(exportingMembersMap.keySet(), "memberA", "memberB");
        });
    }

    @Test
    void testGetUseScopeShouldReturnImportingFileAndSelf() {
        PsiFile exportingFile = configureByText("exportingFile.omt", "queries:\n" +
                "   DEFINE QUERY memberA => '';\n" +
                "   DEFINE QUERY memberB => '';\n");
        PsiFile importingFile = myFixture.addFileToProject("index.omt", "import:\n" +
                "   exportingFile.omt:\n" +
                "   - memberA\n" +
                "   - memberB\n");
        underProgress(() -> {
            SearchScope useScope = exportingFile.getUseScope();
            assertTrue(useScope.contains(exportingFile.getVirtualFile()));
            assertTrue(useScope.contains(importingFile.getVirtualFile()));
        });
    }

    @Test
    void testGetUseScopeShouldReturnImportingTree() {
        PsiFile exportingFile = configureByText("exportingFile.omt", "queries:\n" +
                "   DEFINE QUERY memberA => '';\n" +
                "   DEFINE QUERY memberB => '';\n");
        PsiFile importingFile = myFixture.addFileToProject("index.omt", "import:\n" +
                "   exportingFile.omt:\n" +
                "   - memberA\n" +
                "   - memberB\n");
        PsiFile secondaryImportingFile = myFixture.addFileToProject("index2.omt", "import:\n" +
                "   index.omt:\n" +
                "   - memberA\n" +
                "   - memberB\n");
        underProgress(() -> {
            SearchScope useScope = exportingFile.getUseScope();
            assertTrue(useScope.contains(exportingFile.getVirtualFile()));
            assertTrue(useScope.contains(importingFile.getVirtualFile()));
            assertTrue(useScope.contains(secondaryImportingFile.getVirtualFile()));
        });
    }

    @Test
    void testRecognizedModuleFilenameEndsWith() {
        OMTFile omtFile = configureByText("my.module.omt", "");
        assertTrue(OMTFile.isModuleFileName(omtFile.getName()));
        assertEquals(omtFile.getModuleFile(), omtFile);
    }

    @Test
    void testRecognizedModuleFilenameEquals() {
        OMTFile omtFile = configureByText("module.omt", "");
        assertTrue(OMTFile.isModuleFileName(omtFile.getName()));
        assertEquals(omtFile.getModuleFile(), omtFile);
    }

    @Test
    void testRecognizedInterfaceFilenameEndsWith() {
        OMTFile omtFile = configureByText("my.interface.omt", "");
        assertTrue(OMTFile.isInterfaceFileName(omtFile.getName()));
    }
}
