package com.misset.opp.ttl.psi.impl.spo;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.psi.extend.TTLQualifiedIriResolver;
import com.misset.opp.ttl.stubs.subject.TTLSubjectStub;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class TTLSubjectAbstract extends StubBasedPsiElementBase<TTLSubjectStub> implements TTLSubject {
    protected TTLSubjectAbstract(@NotNull TTLSubjectStub stub, @NotNull IStubElementType<?, ?> nodeType) {
        super(stub, nodeType);
    }

    protected TTLSubjectAbstract(@NotNull ASTNode node) {
        super(node);
    }

    protected TTLSubjectAbstract(TTLSubjectStub stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    @Override
    public String getQualifiedIri() {
        return Optional.ofNullable(getIri()).map(TTLQualifiedIriResolver::getQualifiedIri).orElse(null);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return null;
    }
}
