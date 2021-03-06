package com.misset.opp.ttl.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.stubs.StubIndex;
import com.misset.opp.ttl.psi.TTLIri;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.stubs.index.TTLSubjectStubIndex;
import com.misset.opp.ttl.util.TTLScopeUtil;
import org.jetbrains.annotations.NotNull;

public class TTLObjectClassReference extends PsiReferenceBase.Poly<TTLIri> implements PsiPolyVariantReference {
    public TTLObjectClassReference(TTLIri ttlIri, TextRange textRange) {
        super(ttlIri, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        String qualifiedUri = getElement().getQualifiedUri();
        if (qualifiedUri == null) {
            return ResolveResult.EMPTY_ARRAY;
        }
        return StubIndex.getElements(TTLSubjectStubIndex.KEY,
                        qualifiedUri,
                        getElement().getProject(),
                        TTLScopeUtil.getModelSearchScope(getElement().getProject()),
                        TTLSubject.class)
                .stream()
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }
}
