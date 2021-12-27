package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.prefix.ODTBaseNamespacePrefix;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ODTNamespacePrefixReference extends ODTPolyReferenceBase<ODTBaseNamespacePrefix> {
    Logger LOGGER = Logger.getInstance(ODTNamespacePrefixReference.class);
    public ODTNamespacePrefixReference(@NotNull ODTBaseNamespacePrefix element) {
        super(element, TextRange.allOf(element.getName()), false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return LoggerUtil.computeWithLogger(LOGGER, "Resolving namespace prefix " + myElement.getName(), () -> {
            if (!myElement.isValid()) {
                return ResolveResult.EMPTY_ARRAY;
            }
            // resolve in current ODT file
            // then resolve in OMT using the PrefixProviders
            return resolveInODT()
                    .or(this::resolveFromProvider)
                    .orElse(ResolveResult.EMPTY_ARRAY);
        });
    }

    private Optional<ResolveResult[]> resolveFromProvider() {
        return Optional.of(toResults(myElement.getContainingFile()
                .getHostPrefixNamespace(myElement.getName())));
    }

    private Optional<ResolveResult[]> resolveInODT() {
        ODTFile containingFile = myElement.getContainingFile();
        return containingFile.getLocalNamespacePrefixes()
                .stream()
                // must have the same name
                .filter(namespacePrefix -> Optional.of(getElement().getName())
                        .map(s -> s.equals(namespacePrefix.getName()))
                        .orElse(false))
                .min((o1, o2) -> Integer.compare(o1.getTextOffset(), o2.getTextOffset()) * -1)
                .map(PsiElementResolveResult::createResults);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
