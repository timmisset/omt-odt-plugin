package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class ODTPrefixReferenceBase<T extends PsiElement> extends ODTPolyReferenceBase<T> {
    public ODTPrefixReferenceBase(T element, TextRange rangeInElement, boolean soft) {
        super(element, rangeInElement, soft);
    }

    protected Optional<ResolveResult[]> resolveInODT(@NotNull ODTFile containingFile, @NotNull String name) {
        return containingFile.getLocalNamespacePrefixes()
                .stream()
                // must have the same name
                .filter(namespacePrefix -> Optional.of(name)
                        .map(s -> s.equals(namespacePrefix.getName()))
                        .orElse(false))
                .min((o1, o2) -> Integer.compare(o1.getTextOffset(), o2.getTextOffset()) * -1)
                .map(PsiElementResolveResult::createResults);
    }

    protected ResolveResult[] resolveInOMT(@NotNull ODTFile containingFile,
                                           @NotNull String name) {
        return containingFile.resolveInOMT(OMTPrefixProvider.class,
                        OMTPrefixProvider.KEY,
                        name,
                        OMTPrefixProvider::getPrefixMap)
                .map(this::toResults)
                .orElse(ResolveResult.EMPTY_ARRAY);
    }
}
