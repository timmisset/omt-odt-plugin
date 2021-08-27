package com.misset.opp.omt.psi;

import com.intellij.psi.PsiFile;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.YAMLFileType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OMTFileImplTest extends OMTTestCase {

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
    }

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
                        "           @LOG('Test');");

        Assertions.assertEquals(YAMLFileType.YML, psiFile.getFileType());
    }
}
