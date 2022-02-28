package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlImportMemberDelegate;

import java.util.List;
import java.util.Objects;

public abstract class ODTPolyReferenceBase<T extends PsiElement> extends PsiReferenceBase.Poly<T> implements PsiPolyVariantReference {
    public ODTPolyReferenceBase(T element, TextRange rangeInElement, boolean soft) {
        super(element, rangeInElement, soft);
    }

    protected ResolveResult[] toResults(List<? extends PsiElement> resolvedElements,
                                        boolean resolveToOriginalElement,
                                        boolean resolveToFinalElement) {
        return resolvedElements.stream()
                .filter(Objects::nonNull)
                .map(psiElement -> {
                    PsiElement element = psiElement;
                    if (psiElement instanceof OMTYamlImportMemberDelegate && resolveToFinalElement) {
                        element = ((OMTYamlImportMemberDelegate) psiElement).getFinalElement();
                    }
                    return element != null && resolveToOriginalElement ? element.getOriginalElement() : element;
                })
                .filter(Objects::nonNull)
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    protected ResolveResult[] toResults(List<? extends PsiElement> resolvedElements) {
        return toResults(resolvedElements, true, true);
    }
}
