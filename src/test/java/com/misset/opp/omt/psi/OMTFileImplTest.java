package com.misset.opp.omt.psi;

import com.intellij.psi.PsiFile;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.YAMLFileType;
import org.jetbrains.yaml.psi.impl.YAMLFileImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTFileImplTest extends OMTTestCase {

    @Test
    void getFileTypeOMT() {

        final PsiFile psiFile = configureByText("test.omt",
                "model:\n" +
                        "   Activiteit: !Activity\n" +
                        "       onStart: |\n" +
                        "           @LOG('Test');");

        Assertions.assertEquals(OMTFileType.INSTANCE, psiFile.getFileType());
    }

    @Test
    void getFileTypeYAML() {
        final PsiFile psiFile = configureByText("test.yml",
                "model:\n" +
                        "   Activiteit: !Activity\n" +
                        "       onStart: |\n" +
                        "           @LOG('Test');", false, YAMLFileImpl.class);

        Assertions.assertEquals(YAMLFileType.YML, psiFile.getFileType());
    }
}
