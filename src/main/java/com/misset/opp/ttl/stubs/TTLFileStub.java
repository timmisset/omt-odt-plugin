package com.misset.opp.ttl.stubs;

import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.stubs.PsiFileStubImpl;
import com.misset.opp.ttl.psi.TTLFile;

public class TTLFileStub extends PsiFileStubImpl<TTLFile> implements PsiFileStub<TTLFile> {
    public TTLFileStub(TTLFile file) {
        super(file);
    }
}
