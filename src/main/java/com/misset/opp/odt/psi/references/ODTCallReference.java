package com.misset.opp.odt.psi.references;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.misset.opp.odt.psi.impl.calls.ODTBaseCall;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

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
    public @Nullable PsiElement resolve() {
        // An ODT call is made to either an OMT Callable element such as an Activity, Procedure within the host OMT file
        // or directly to a built-in command-call or operator

        // the order to resolve the call name is:
        // - built-in
        // - host -> OMTFile.modelItem.queries || OMTFile.modelItem.commands
        // - host -> OMTFile.model
        // - host -> OMTFile.queries || OMTFile.commands
        // - host -> OMTFile.import

        // a local call should not shadow a built-in version and an error will be displayed by Inspection of the Command/Query
        final InjectedLanguageManager instance = InjectedLanguageManager.getInstance(myElement.getProject());
        final PsiLanguageInjectionHost injectionHost = instance.getInjectionHost(myElement.getContainingFile());
        if(!(injectionHost instanceof YAMLPsiElement)) {
            return null;
        }
        final YAMLPsiElement yamlPsiElement = (YAMLPsiElement) injectionHost;
        return resolveFromOMT(yamlPsiElement)
                .orElse(null);
    }

    private Optional<PsiElement> resolveFromOMT(YAMLPsiElement injectionHost) {
        final LinkedHashMap<YAMLMapping, OMTCallableProvider> linkedHashMap = collectCallableProviders(injectionHost);
        for(YAMLMapping mapping : linkedHashMap.keySet()) {
            OMTCallableProvider callableProvider = linkedHashMap.get(mapping);
            final HashMap<String, List<PsiElement>> callableMap = callableProvider.getCallableMap(mapping);
            if(callableMap.containsKey(myElement.getCallId())) {
                return Optional.of(callableMap.get(myElement.getCallId()).get(0));
            }
        }
        return Optional.empty();
    }
}
