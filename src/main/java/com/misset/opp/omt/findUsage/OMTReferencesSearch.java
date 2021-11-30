package com.misset.opp.omt.findUsage;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class OMTReferencesSearch {
    private static final Key<CachedValue<List<PsiLanguageInjectionHost>>> INJECTED_HOSTS = new Key<>("INJECTED_HOSTS");

    protected <T extends PsiElement> void addReferencesFromInjectedLanguage(
            PsiElement elementToSearch,
            PsiElement scope,
            Class<T> injectedElementType,
            @NotNull Processor<? super PsiReference> consumer) {
        CachedValuesManager.getCachedValue(scope,
                        INJECTED_HOSTS,
                        () -> {
                            final Collection<PsiLanguageInjectionHost> injectionHosts = PsiTreeUtil.findChildrenOfType(scope,
                                    PsiLanguageInjectionHost.class);
                            final List<PsiLanguageInjectionHost> injectedHosts = injectionHosts.stream()
                                    .filter(PsiLanguageInjectionHost::isValidHost)
                                    .filter(YAMLValue.class::isInstance)
                                    .collect(Collectors.toList());
                            return new CachedValueProvider.Result<>(injectedHosts, scope);
                        })

                .forEach(host -> addReferencesFromInjectedLanguage(elementToSearch,
                        (YAMLValue) host,
                        injectedElementType,
                        consumer));
    }

    private <T extends PsiElement> void addReferencesFromInjectedLanguage(PsiElement target,
                                                                          YAMLValue value,
                                                                          Class<T> injectedElementType,
                                                                          @NotNull Processor<? super PsiReference> consumer) {
        final Collection<T> variables = OMTProviderUtil.getInjectedContent(value, injectedElementType);
        variables.stream()
                .map(PsiElement::getReference)
                .filter(Objects::nonNull)
                .filter(psiReference -> psiReference.isReferenceTo(target))
                .forEach(consumer::process);
    }

}
