package com.misset.opp.ttl.psi.impl.iri;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.ttl.TTLElementGenerator;
import com.misset.opp.ttl.psi.TTLIriReference;
import com.misset.opp.ttl.psi.TTLObject;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.psi.reference.TTLClassReference;
import com.misset.opp.util.UriPatternUtil;
import org.jetbrains.annotations.NotNull;

public abstract class TTLIriReferenceAbstract extends TTLIriAbstract implements TTLIriReference {
    protected TTLIriReferenceAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        String qualifiedIri = getQualifiedIri();
        if (!UriPatternUtil.isUri(qualifiedIri)) {
            return null;
        }
        String namespace = UriPatternUtil.getNamespace(qualifiedIri);
        TTLIriReference iriReference = TTLElementGenerator.getInstance(getProject()).getIriReference(namespace + name);
        if (iriReference != null) {
            return replace(iriReference);
        }
        return null;
    }

    /**
     * Provides a reference when this IRIRef is contained in a TTLIri that is being used
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
    public String getQualifiedIri() {
        return getText().substring(1, getTextLength() - 1);
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return GlobalSearchScope.allScope(getProject());
    }
}
