package com.misset.opp.ttl.psi.impl.iri;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.ttl.psi.TTLCurie;
import com.misset.opp.ttl.psi.TTLPrefix;
import com.misset.opp.ttl.psi.reference.TTLPrefixReference;
import org.jetbrains.annotations.NotNull;

/**
 * A TTLPrefix can be used in a prefix declaration or usage (in a curie)
 */
public abstract class TTLPrefixAbstract extends ASTWrapperPsiElement implements TTLPrefix {
    protected TTLPrefixAbstract(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * Provides a reference when this Prefix is part of a Curie
     */
    @Override
    public TTLPrefixReference getReference() {
        if (getParent() instanceof TTLCurie) {
            return new TTLPrefixReference(this, TextRange.allOf(getText()));
        }
        return null;
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return null;
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return GlobalSearchScope.fileScope(getContainingFile());
    }
}
