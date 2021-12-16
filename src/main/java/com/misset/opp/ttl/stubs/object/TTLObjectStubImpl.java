package com.misset.opp.ttl.stubs.object;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.misset.opp.ttl.psi.TTLObject;
import com.misset.opp.ttl.psi.TTLTypes;
import org.jetbrains.annotations.Nullable;

public class TTLObjectStubImpl extends StubBase<TTLObject> implements TTLObjectStub {
    private final String iri;
    private final boolean isPredicate;
    private final boolean isObject;
    private final String subjectIri;

    protected TTLObjectStubImpl(@Nullable StubElement parent,
                                String iri,
                                boolean isPredicate,
                                boolean isObject,
                                String subjectIri) {
        super(parent, (TTLObjectStubElementType) TTLTypes.OBJECT);
        this.iri = iri;
        this.isPredicate = isPredicate;
        this.isObject = isObject;
        this.subjectIri = subjectIri;
    }

    @Override
    public String getQualifiedUri() {
        return iri;
    }

    @Override
    public boolean isPredicate() {
        return isPredicate;
    }

    @Override
    public boolean isObject() {
        return isObject;
    }

    @Override
    public String getSubjectIri() {
        return subjectIri;
    }


}
