package com.misset.opp.ttl.psi.spo;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.psi.*;
import com.misset.opp.ttl.psi.iri.TTLIriHolder;
import com.misset.opp.ttl.stubs.object.TTLObjectStub;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The SHACL model has:
 * - sh:path OBJECT <-- this is the predicate in the simplified model
 * - sh:class / sh:datatype <-- this is the object in the simplified model
 */
public abstract class TTLBaseObject extends StubBasedPsiElementBase<TTLObjectStub> implements TTLObject {
    public TTLBaseObject(@NotNull TTLObjectStub stub, @NotNull IStubElementType<?, ?> nodeType) {
        super(stub, nodeType);
    }

    public TTLBaseObject(@NotNull ASTNode node) {
        super(node);
    }

    public TTLBaseObject(TTLObjectStub stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    public String getQualifiedUri() {
        return Optional.ofNullable(getIri()).map(TTLIriHolder::getQualifiedUri).orElse(null);
    }

    @Override
    public boolean isPredicate() {
        return getFromStubOrElse(TTLObjectStub::isPredicate,
                () -> hasShaclPredicate(OppModel.INSTANCE.SHACL_PATH.getURI()));
    }

    @Override
    public String getSubjectIri() {
        return getFromStubOrElse(TTLObjectStub::getSubjectIri,
                () -> Optional.ofNullable(PsiTreeUtil.getParentOfType(this, TTLSubject.class))
                        .map(TTLSPO::getQualifiedUri)
                        .orElse(null));
    }

    private <T> T getFromStubOrElse(Function<TTLObjectStub, T> fromStub, Supplier<T> fromPsi) {
        if (getStub() != null) {
            return fromStub.apply(getStub());
        } else {
            return fromPsi.get();
        }
    }

    @Override
    public boolean isObject() {
        return getFromStubOrElse(TTLObjectStub::isPredicate,
                () -> hasShaclPredicate(OppModel.INSTANCE.SHACL_CLASS.getURI()));
    }

    private boolean hasShaclPredicate(String iri) {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(this, TTLPredicateObject.class))
                .map(TTLPredicateObject::getVerb)
                .map(TTLVerb::getPredicate)
                .map(TTLPredicate::getIri)
                .map(TTLIriHolder::getQualifiedUri)
                .map(iri::equals)
                .orElse(false);
    }
}
