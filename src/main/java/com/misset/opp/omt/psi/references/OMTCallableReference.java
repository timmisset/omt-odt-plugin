package com.misset.opp.omt.psi.references;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlImportMemberDelegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Map;
import java.util.Optional;

public class OMTCallableReference extends OMTPlainTextReference {

    public OMTCallableReference(@NotNull YAMLPlainTextImpl element) {
        super(element);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return multiResolveToOriginal(true, true);
    }

    public PsiElement resolve(boolean resolveToOriginalElement, boolean resolveToFinalElement) {
        ResolveResult[] resolveResults = multiResolveToOriginal(resolveToOriginalElement, resolveToFinalElement);
        return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
    }

    private ResolveResult @NotNull [] multiResolveToOriginal(boolean resolveToOriginalElement,
                                                             boolean resolveToFinalElement) {
        return resolveFromProvider(resolveToOriginalElement, resolveToFinalElement)
                .orElse(ResolveResult.EMPTY_ARRAY);
    }

    private Optional<ResolveResult[]> resolveFromProvider(boolean resolveToOriginalElement,
                                                          boolean resolveToFinalElement) {
        Map<YAMLMapping, OMTCallableProvider> providerMap =
                OMTMetaTreeUtil.collectMetaParents(myElement, YAMLMapping.class, OMTCallableProvider.class);
        return OMTMetaTreeUtil.resolveProvider(providerMap, myElement.getText(), OMTCallableProvider::getCallableMap)
                .map(psiCallables -> toResults(psiCallables, resolveToOriginalElement, resolveToFinalElement));
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        boolean resolveToFinalElement = !(element instanceof OMTYamlImportMemberDelegate);
        return Optional.ofNullable(resolve(true, resolveToFinalElement))
                .map(element.getOriginalElement()::equals)
                .orElse(false);
    }
}
