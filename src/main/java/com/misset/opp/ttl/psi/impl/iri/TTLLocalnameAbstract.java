package com.misset.opp.ttl.psi.impl.iri;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.ttl.TTLElementGenerator;
import com.misset.opp.ttl.psi.TTLIri;
import com.misset.opp.ttl.psi.TTLLocalname;
import com.misset.opp.ttl.psi.TTLObject;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.psi.reference.TTLClassReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The Localname part of a curie.
 */
public abstract class TTLLocalnameAbstract extends ASTWrapperPsiElement implements TTLLocalname {
    protected TTLLocalnameAbstract(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * Provides a reference when this Localname is contained in a TTLIri that is being used
     * as Object (Subject-Predicate-Object) with a class based predicate.
     */
    @Override
    public PsiReference getReference() {
        if (PsiTreeUtil.getParentOfType(this, TTLObject.class, TTLSubject.class) instanceof TTLObject) {
            return new TTLClassReference(this, TextRange.allOf(getText()));
        }
        return null;
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        TTLLocalname replacement = TTLElementGenerator.getInstance(getProject()).getLocalName(name);
        return replacement != null ? replace(replacement) : null;
    }

    @Override
    public @Nullable String getQualifiedIri() {
        return ((TTLIri) getParent()).getQualifiedIri();
    }

    @Override
    public String getName() {
        return getText();
    }
}
