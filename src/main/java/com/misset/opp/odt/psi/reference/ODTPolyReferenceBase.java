package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlImportMemberDelegate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public abstract class ODTPolyReferenceBase<T extends PsiElement> extends PsiReferenceBase.Poly<T> implements PsiPolyVariantReference {
    protected ODTPolyReferenceBase(T element, TextRange rangeInElement, boolean soft) {
        super(element, rangeInElement, soft);
    }

    protected ResolveResult[] toResults(Collection<? extends PsiElement> resolvedElements,
                                        boolean resolveToOriginalElement,
                                        boolean resolveToFinalElement) {
        if (resolvedElements == null) {
            return ResolveResult.EMPTY_ARRAY;
        }
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

    protected ResolveResult[] toResults(Collection<? extends PsiElement> resolvedElements) {
        return toResults(resolvedElements, true, true);
    }

    /**
     * Uses the multiResolve method to obtain one or more possible targets for the reference
     * When more than 1 result is present it will use the proximity to the Reference's element to determine
     * which one is picked. This is done by the comparison that:
     * - same file is preferred over different file
     * - psiTree deep is preferred over shallow
     * - element offset high score is preferred over low score
     */
    protected PsiElement getResultByProximity() {
        ResolveResult[] resolveResults = multiResolve(false);
        if (resolveResults.length == 1) {
            return resolveResults[0].getElement();
        }
        if (resolveResults.length == 0) {
            return null;
        }
        Arrays.sort(resolveResults, this::compareResolveResult);
        return resolveResults[0].getElement();
    }

    private int compareResolveResult(ResolveResult resultA, ResolveResult resultB) {
        PsiFile containingFile = myElement.getContainingFile();
        PsiElement elementA = resultA.getElement();
        PsiElement elementB = resultB.getElement();
        if (elementA == null || elementB == null) {
            return 0;
        }
        if (elementA.getContainingFile() == containingFile &&
                elementB.getContainingFile() != containingFile) {
            return -1;
        } else if (elementB.getContainingFile() == containingFile &&
                elementA.getContainingFile() != containingFile) {
            return 1;
        } else if (elementA.getContainingFile() == elementB.getContainingFile()) {
            int depthA = PsiTreeUtil.getDepth(elementA, elementA.getContainingFile());
            int depthB = PsiTreeUtil.getDepth(elementB, elementB.getContainingFile());
            if (depthA > depthB) {
                return -1;
            } else if (depthA < depthB) {
                return 1;
            } else {
                return elementA.getTextOffset() > elementB.getTextOffset() ? -1 : 1;
            }
        } else {
            return 0;
        }
    }
}
