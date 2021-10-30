package com.misset.opp.omt.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.YAMLFileType;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

class OMTFileImplTest extends OMTTestCase {

    @Test
    void getFileTypeOMT() {

        final PsiFile psiFile = configureByText("test.omt", "model:\n");
        Assertions.assertEquals(OMTFileType.INSTANCE, psiFile.getFileType());
    }

    @Test
    void getFileTypeYAML() {
        final PsiFile psiFile = configureByText("test.yml", "model:\n", false, YAMLFileImpl.class);
        Assertions.assertEquals(YAMLFileType.YML, psiFile.getFileType());
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
        final HashMap<String, List<PsiElement>> exportingMembersMap = omtFile.getExportingMembersMap();
        assertNotEmpty(exportingMembersMap.keySet());
        Assertions.assertTrue(exportingMembersMap.containsKey("@MyActivity"));
        Assertions.assertTrue(exportingMembersMap.containsKey("@exportedCommand"));
        Assertions.assertTrue(exportingMembersMap.containsKey("exportedQuery"));
        Assertions.assertFalse(exportingMembersMap.containsKey("@notExportedCommand"));
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
                "   - MyActivity\n";
        final OMTFile omtFile = configureByText(importingFile);
        final HashMap<String, List<PsiElement>> exportingMembersMap = omtFile.getExportingMembersMap();
        Assertions.assertTrue(exportingMembersMap.containsKey("@MyActivity"));
        Assertions.assertNotNull(exportingMembersMap.get("@MyActivity"));
    }

}
