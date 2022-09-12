package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTNamespacePrefix;
import com.misset.opp.resolvable.psi.PsiPrefix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ODTNamespacePrefixReference extends ODTPolyReferenceBase<ODTNamespacePrefix> {
    public ODTNamespacePrefixReference(@NotNull ODTNamespacePrefix element) {
        super(element, TextRange.allOf(element.getName()), false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return multiResolve(true, true);
    }

    @Override
    public @Nullable PsiElement resolve() {
        return resolve(true);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }

    @Override
    public PsiElement resolve(boolean resolveToOriginalElement) {
        return resolve(resolveToOriginalElement, true);
    }

    @Override
    public PsiElement resolve(boolean resolveToOriginalElement, boolean resolveToFinalElement) {
        return Optional.ofNullable(getResultByProximity(resolveToOriginalElement, resolveToFinalElement)).map(PsiElement::getOriginalElement).orElse(null);
    }

    @Override
    public ResolveResult[] multiResolve(boolean resolveToOriginalElement, boolean resolveToFinalElement) {
        if (!myElement.isValid() || !(myElement.getContainingFile() instanceof ODTFile)) {
            return ResolveResult.EMPTY_ARRAY;
        }

        ODTFile file = (ODTFile) myElement.getContainingFile();
        List<PsiPrefix> prefixes = file.getPrefixes(myElement.getName()).stream()
                .filter(psiPrefix -> file.isAccessible(myElement, psiPrefix))
                .collect(Collectors.toList());
        return toResults(prefixes);
    }
}
