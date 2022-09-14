package com.misset.opp.omt.psi.impl;

import com.intellij.psi.PsiFile;
import com.misset.opp.testcase.BasicTestCase;
import org.jetbrains.yaml.YAMLFileType;
import org.jetbrains.yaml.psi.YAMLFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class YamlFileImplTest extends BasicTestCase<YAMLFile> {
    public YamlFileImplTest() {
        super(YAMLFileType.YML);
    }

    @Override
    protected YAMLFile castToFile(PsiFile file) {
        return (YAMLFile) file;
    }

    @Test
    void getFileTypeYAML() {
        final PsiFile psiFile = configureByText("test.yml", "model:\n", false);
        Assertions.assertEquals(YAMLFileType.YML, psiFile.getFileType());
    }
}
