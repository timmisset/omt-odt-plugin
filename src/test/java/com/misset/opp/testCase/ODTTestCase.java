package com.misset.opp.testCase;

import com.intellij.psi.PsiFile;
import com.misset.opp.odt.ODTFileType;
import com.misset.opp.odt.psi.ODTFile;

public class ODTTestCase extends BasicTestCase<ODTFile> {
    public ODTTestCase() {
        super(ODTFileType.INSTANCE);
    }

    @Override
    protected ODTFile castToFile(PsiFile file) {
        return (ODTFile) file;
    }
}
