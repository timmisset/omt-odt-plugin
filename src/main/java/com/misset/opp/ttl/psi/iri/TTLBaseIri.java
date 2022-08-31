package com.misset.opp.ttl.psi.iri;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.ttl.psi.TTLIri;
import com.misset.opp.ttl.psi.TTLObject;
import com.misset.opp.ttl.psi.prefix.TTLPrefixedNameHolder;
import com.misset.opp.ttl.psi.reference.TTLObjectClassReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class TTLBaseIri extends ASTWrapperPsiElement implements TTLIri, PsiNamedElement {
    protected TTLBaseIri(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    @Nullable
    public String getQualifiedUri() {
        return Optional.ofNullable(getPrefixedName())
                .map(TTLPrefixedNameHolder::getQualifiedUri)
                .orElse(getTrimmedIriRef());
    }

    @Override
    public PsiReference getReference() {
        TTLObject ttlObject = PsiTreeUtil.getParentOfType(this, TTLObject.class);
        if (ttlObject == null || !ttlObject.isObjectClass()) {
            return null;
        }

        TextRange textRange = TextRange.create(getTextOffset() - super.getTextOffset(), getTextLength());
        return new TTLObjectClassReference(this, textRange);
    }

    private String getTrimmedIriRef() {
        if (getPrefixedName() != null) {
            return null;
        }
        String iri = getFirstChild().getText();
        return iri.substring(1, iri.length() - 1);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Override
    public String getName() {
        return Optional.ofNullable(getPrefixedName())
                .map(PsiNamedElement::getName)
                .orElse(getTrimmedIriRef());
    }

    @Override
    public int getTextOffset() {
        return Optional.ofNullable(getPrefixedName())
                .map(PsiElement::getTextOffset)
                .orElse(super.getTextOffset() + 1);
    }
}
