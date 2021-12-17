package com.misset.opp.ttl.psi.prefix;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.ttl.psi.TTLPrefixedName;
import com.misset.opp.ttl.psi.reference.TTLPrefixReference;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class TTLBasePrefixedName extends ASTWrapperPsiElement implements TTLPrefixedNameHolder, TTLPrefixedName, PsiNamedElement {
    public TTLBasePrefixedName(@NotNull ASTNode node) {
        super(node);
    }


    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Override
    public PsiReference getReference() {
        return new TTLPrefixReference(this, getPrefixIdTextRange());
    }

    @Override
    public String getIri() {
        return Optional.ofNullable(getReference())
                .map(PsiReference::resolve)
                .filter(TTLBasePrefixId.class::isInstance)
                .map(TTLBasePrefixId.class::cast)
                .map(TTLBasePrefixId::getIri)
                .map(TTLResourceUtil::fromIriRef)
                .orElse(null);
    }

    public String getQualifiedUri() {
        return getIri() + getText().substring(getPrefixIdTextRange().getEndOffset());
    }

    @Override
    public String getPrefixId() {
        String id = getPrefixIdTextRange().substring(getText());
        if (id.isBlank()) {
            return "unnamed";
        }
        return id;
    }

    @Override
    public int getTextOffset() {
        return getPrefixIdTextRange().getEndOffset() + super.getTextOffset();
    }

    @Override
    public String getName() {
        return getText().substring(getTextOffset() - super.getTextOffset());
    }

    @Override
    public TextRange getPrefixIdTextRange() {
        int index = getText().indexOf(":");
        if (index == -1) {
            return TextRange.EMPTY_RANGE;
        }
        return TextRange.create(0, index + 1);
    }
}
