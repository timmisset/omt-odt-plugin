package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.model.util.OntologyScopeUtil;
import com.misset.opp.odt.psi.resolvable.querystep.ODTResolvableQualifiedUriStep;
import com.misset.opp.ttl.psi.TTLLocalname;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.stubs.index.TTLSubjectStubIndex;
import org.jetbrains.annotations.NotNull;

public class ODTTTLSubjectReference extends PsiReferenceBase.Poly<ODTResolvableQualifiedUriStep> implements PsiPolyVariantReference {
    public ODTTTLSubjectReference(ODTResolvableQualifiedUriStep element, TextRange textRange) {
        super(element, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        String fullyQualifiedUri = myElement.getFullyQualifiedUri();
        if (fullyQualifiedUri == null) {
            return ResolveResult.EMPTY_ARRAY;
        }
        return StubIndex.getElements(
                        TTLSubjectStubIndex.KEY,
                        fullyQualifiedUri,
                        myElement.getProject(),
                        OntologyScopeUtil.getModelSearchScope(myElement.getProject()),
                        TTLSubject.class
                ).stream()
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        if (element instanceof TTLLocalname) {
            String fullyQualifiedUri = myElement.getFullyQualifiedUri();
            return fullyQualifiedUri != null &&
                    fullyQualifiedUri.equals(((TTLLocalname) element).getQualifiedIri());
        }
        return super.isReferenceTo(element);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
