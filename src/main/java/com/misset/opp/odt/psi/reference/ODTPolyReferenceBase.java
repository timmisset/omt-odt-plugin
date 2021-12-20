package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;

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

    protected ResolveResult[] toResults(List<? extends PsiElement> resolvedElements, boolean resolveToOriginalElement) {
        return resolvedElements.stream()
                .map(psiCallable -> resolveToOriginalElement ? psiCallable.getOriginalElement() : psiCallable)
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    protected ResolveResult[] toResults(List<? extends PsiElement> resolvedElements) {
        return toResults(resolvedElements, true);
    }
}
