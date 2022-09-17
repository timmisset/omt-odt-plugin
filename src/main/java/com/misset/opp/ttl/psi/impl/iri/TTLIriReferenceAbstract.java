package com.misset.opp.ttl.psi.impl.iri;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.ttl.psi.TTLIriReference;
import com.misset.opp.ttl.psi.TTLObject;
import com.misset.opp.ttl.psi.reference.TTLClassReference;
import org.jetbrains.annotations.NotNull;

public abstract class TTLIriReferenceAbstract extends TTLIriAbstract implements TTLIriReference {
    protected TTLIriReferenceAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return null;
    }

    /**
     * Provides a reference when this IRIRef is contained in a TTLIri that is being used
     * as Object (Subject-Predicate-Object) with a class based predicate.
     */
    @Override
    public PsiReference getReference() {
        TTLObject object = PsiTreeUtil.getParentOfType(this, TTLObject.class);
        if (object != null && object.isClass()) {
            return new TTLClassReference(this, TextRange.allOf(getText()));
        }
        return null;
    }

    @Override
    public String getQualifiedIri() {
        return getText().substring(1, getTextLength() - 1);
    }
}
