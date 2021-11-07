package com.misset.opp.testCase;

import com.intellij.psi.PsiFile;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.omt.OMTFileType;

public class ODTTestCase extends BasicTestCase<ODTFile> {
    public ODTTestCase() {
        super(OMTFileType.INSTANCE);
    }

    @Override
    protected ODTFile castToFile(PsiFile file) {
        return (ODTFile) file;
    }
}
