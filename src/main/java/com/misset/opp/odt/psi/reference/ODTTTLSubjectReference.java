package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.stubs.StubIndex;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQualifiedUriStep;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.stubs.index.TTLSubjectStubIndex;
import com.misset.opp.ttl.util.TTLScopeUtil;
import org.jetbrains.annotations.NotNull;

public class ODTTTLSubjectReference extends PsiReferenceBase.Poly<ODTResolvableQualifiedUriStep> implements PsiPolyVariantReference {
    public ODTTTLSubjectReference(ODTResolvableQualifiedUriStep element, TextRange textRange) {
        super(element, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        ODTResolvableQualifiedUriStep element = getElement();
        String fullyQualifiedUri = element.getFullyQualifiedUri();
        if (fullyQualifiedUri == null) {
            return ResolveResult.EMPTY_ARRAY;
        }
        return StubIndex.getElements(
                        TTLSubjectStubIndex.KEY,
                        fullyQualifiedUri,
                        element.getProject(),
                        TTLScopeUtil.getModelSearchScope(myElement.getProject()),
                        TTLSubject.class
                ).stream()
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }


}
