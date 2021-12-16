package com.misset.opp.ttl.stubs.subject;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.misset.opp.ttl.TTLLanguage;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.psi.impl.TTLSubjectImpl;
import com.misset.opp.ttl.stubs.index.TTLSubjectStubIndex;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class TTLSubjectStubElementType extends IStubElementType<TTLSubjectStub, TTLSubject> {
    public TTLSubjectStubElementType(String debugName) {
        super(debugName, TTLLanguage.INSTANCE);
    }

    @Override
    public TTLSubject createPsi(@NotNull TTLSubjectStub stub) {
        return new TTLSubjectImpl(stub, this);
    }

    @Override
    public @NotNull TTLSubjectStub createStub(@NotNull TTLSubject psi, StubElement<? extends PsiElement> parentStub) {
        return new TTLSubjectStubImpl(parentStub, psi.getQualifiedUri());
    }

    @Override
    public @NotNull String getExternalId() {
        return "TTL.Subject";
    }

    @Override
    public void serialize(@NotNull TTLSubjectStub stub, @NotNull StubOutputStream dataStream) throws IOException {
        dataStream.writeName(stub.getQualifiedUri());
    }

    @Override
    public @NotNull TTLSubjectStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
        return new TTLSubjectStubImpl(parentStub, dataStream.readNameString());
    }

    @Override
    public void indexStub(@NotNull TTLSubjectStub stub, @NotNull IndexSink sink) {
        String qualifiedUri = stub.getQualifiedUri();
        if (qualifiedUri == null) {
            return;
        }
        sink.occurrence(TTLSubjectStubIndex.KEY, qualifiedUri);
    }
}
