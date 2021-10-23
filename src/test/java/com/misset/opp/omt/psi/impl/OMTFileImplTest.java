package com.misset.opp.omt.psi.impl;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OMTFileImplTest extends OMTTestCase {

    @Test
    void testGetExportingMembersMapReturnsAllExportableMembers() {
        String content = "model: \n" +
                "   MyActivity: !Activity\n" +
                "       title: Title\n" +
                "       commands: |\n" +
                "           DEFINE COMMAND notExportedCommand => {}\n" +
                "commands:\n" +
                "   DEFINE COMMAND exportedCommand => {}";
        final OMTFile omtFile = configureByText(content);
        final HashMap<String, List<PsiElement>> exportingMembersMap = omtFile.getExportingMembersMap();
        assertNotEmpty(exportingMembersMap.keySet());
        Assertions.assertTrue(exportingMembersMap.containsKey("@MyActivity"));
        Assertions.assertTrue(exportingMembersMap.containsKey("@exportedCommand"));
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
