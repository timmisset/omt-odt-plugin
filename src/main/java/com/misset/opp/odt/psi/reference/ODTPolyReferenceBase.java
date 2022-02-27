package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlImportMemberDelegate;

import java.util.List;

public abstract class ODTPolyReferenceBase<T extends PsiElement> extends PsiReferenceBase.Poly<T> implements PsiPolyVariantReference {

    public ODTPolyReferenceBase(T psiElement) {
        super(psiElement);
    }

    public ODTPolyReferenceBase(T element, boolean soft) {
        super(element, soft);
    }

    public ODTPolyReferenceBase(T element, TextRange rangeInElement, boolean soft) {
        super(element, rangeInElement, soft);
    }

    protected ResolveResult[] toResults(List<? extends PsiElement> resolvedElements,
                                        boolean resolveToOriginalElement,
                                        boolean resolveToFinalElement) {
        return resolvedElements.stream()
                .map(psiElement -> {
                    PsiElement element = psiElement;
                    if (psiElement instanceof OMTYamlImportMemberDelegate && resolveToFinalElement) {
                        element = ((OMTYamlImportMemberDelegate) psiElement).getFinalElement();
                    }
                    return resolveToOriginalElement ? element.getOriginalElement() : element;
                })
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    protected ResolveResult[] toResults(List<? extends PsiElement> resolvedElements) {
        return toResults(resolvedElements, true, true);
    }
}
