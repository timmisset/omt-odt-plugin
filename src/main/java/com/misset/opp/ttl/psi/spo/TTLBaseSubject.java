package com.misset.opp.ttl.psi.spo;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.psi.iri.TTLIriHolder;
import com.misset.opp.ttl.stubs.subject.TTLSubjectStub;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class TTLBaseSubject extends StubBasedPsiElementBase<TTLSubjectStub> implements TTLSubject {
    protected TTLBaseSubject(@NotNull TTLSubjectStub stub, @NotNull IStubElementType<?, ?> nodeType) {
        super(stub, nodeType);
    }

    protected TTLBaseSubject(@NotNull ASTNode node) {
        super(node);
    }

    protected TTLBaseSubject(TTLSubjectStub stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    public String getQualifiedUri() {
        return Optional.ofNullable(getIri()).map(TTLIriHolder::getQualifiedUri).orElse(null);
    }

    @Override
    public boolean isSubject() {
        return true;
    }

    @Override
    public String getSubjectIri() {
        return getQualifiedUri();
    }
}
