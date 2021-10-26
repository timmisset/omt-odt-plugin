package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.misset.opp.odt.psi.impl.call.ODTBaseCall;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static com.misset.opp.odt.ODTMultiHostInjector.getInjectionHost;
import static com.misset.opp.omt.meta.OMTMetaTreeUtil.collectCallableProviders;

public class ODTCallReference extends PsiReferenceBase<ODTBaseCall> implements PsiPolyVariantReference {
    public ODTCallReference(@NotNull ODTBaseCall element,
                            TextRange rangeInElement) {
        super(element, rangeInElement);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return new ResolveResult[0];
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        return super.isReferenceTo(element);
    }

    @Override
    public @Nullable PsiElement resolve() {
        // An ODT call is made to either an OMT Callable element such as an Activity, Procedure within the host OMT file
        // or directly to a built-in command-call or operator

        // the order to resolve the call name is:
        // - built-in
        // - host -> OMTFile.modelItem.queries || OMTFile.modelItem.commands
        // - host -> OMTFile.model
        // - host -> OMTFile.queries || OMTFile.commands
        // - host -> OMTFile.import

        return Optional.ofNullable(getInjectionHost(myElement))
                .map(this::resolveFromOMT)
                .orElse(null);
    }

    private PsiElement resolveFromOMT(YAMLPsiElement injectionHost) {
        final LinkedHashMap<YAMLMapping, OMTCallableProvider> linkedHashMap = collectCallableProviders(injectionHost);
        for(YAMLMapping mapping : linkedHashMap.keySet()) {
            OMTCallableProvider callableProvider = linkedHashMap.get(mapping);
            final HashMap<String, List<PsiElement>> callableMap = callableProvider.getCallableMap(mapping);
            if(callableMap.containsKey(myElement.getCallId())) {
                return callableMap.get(myElement.getCallId()).get(0);
            }
        }
        return null;
    }
}
