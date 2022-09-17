package com.misset.opp.ttl.psi.impl.iri;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.ttl.psi.TTLCurie;
import com.misset.opp.ttl.psi.extend.TTLDeclarePrefix;
import com.misset.opp.ttl.psi.reference.TTLPrefixReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * By default, the TTLIriAbstract implements method for the IRIRef scenario.
 */
public abstract class TTLCurieAbstract extends TTLIriAbstract implements TTLCurie {
    protected TTLCurieAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    @Nullable
    public String getQualifiedIri() {
        String localName = getLocalname().getText();
        return Optional.ofNullable(getPrefix().getReference())
                .map(TTLPrefixReference.class::cast)
                .map(TTLPrefixReference::resolve)
                .map(PsiElement::getParent)
                .filter(TTLDeclarePrefix.class::isInstance)
                .map(TTLDeclarePrefix.class::cast)
                .map(ttlDeclarePrefix -> ttlDeclarePrefix.getQualifiedIri(localName))
                .orElse(null);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return null;
    }
}
