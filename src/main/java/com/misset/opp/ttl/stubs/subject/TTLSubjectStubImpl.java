package com.misset.opp.ttl.stubs.subject;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.psi.TTLTypes;
import org.jetbrains.annotations.Nullable;

public class TTLSubjectStubImpl extends StubBase<TTLSubject> implements TTLSubjectStub {
    private final String iri;

    protected TTLSubjectStubImpl(@Nullable StubElement parent, String iri) {
        super(parent, (TTLSubjectStubElementType) TTLTypes.SUBJECT);
        this.iri = iri;
    }

    @Override
    public String getQualifiedUri() {
        return iri;
    }
}
