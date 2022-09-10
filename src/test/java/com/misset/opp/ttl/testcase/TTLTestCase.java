package com.misset.opp.ttl.testcase;

import com.intellij.psi.PsiFile;
import com.misset.opp.testcase.BasicTestCase;
import com.misset.opp.ttl.TTLFileType;
import com.misset.opp.ttl.psi.TTLFile;

public abstract class TTLTestCase extends BasicTestCase<TTLFile> {
    public TTLTestCase() {
        super(TTLFileType.INSTANCE);
    }

    @Override
    protected TTLFile castToFile(PsiFile file) {
        return (TTLFile) file;
    }


}
