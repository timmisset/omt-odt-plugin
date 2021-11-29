package com.misset.opp.omt.symbols;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.intellij.util.QueryExecutor;
import com.misset.opp.callable.psi.PsiVariable;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

/**
 * Declarations of variables made in the OMT language are not discovered in the injected language
 * Use this QueryExecutor to find any reference to the variable declaration.
 */
public class OMTReferencesSearch implements QueryExecutor<PsiReference, ReferencesSearch.SearchParameters> {

    @Override
    public boolean execute(ReferencesSearch.@NotNull SearchParameters queryParameters,
                           @NotNull Processor<? super PsiReference> consumer) {
        return ReadAction.compute(() -> runSearchInReadAction(queryParameters, consumer));
    }

    private boolean runSearchInReadAction(ReferencesSearch.@NotNull SearchParameters queryParameters,
                                          @NotNull Processor<? super PsiReference> consumer) {
        final PsiElement element = queryParameters.getElementToSearch();
        final OMTMetaTypeProvider metaTypeProvider = OMTMetaTypeProvider.getInstance(element.getProject());
        final YamlMetaType metaType = Optional.of(element)
                .filter(YAMLValue.class::isInstance)
                .map(e -> metaTypeProvider.getValueMetaType((YAMLValue) element))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .orElse(null);

        if (metaType instanceof OMTNamedVariableMetaType) {
            // now look for it in all injected content in the provider of this variable, i.e. the ModelItem
            final YAMLMapping variableProvider = getVariableProvider(element);

            final Collection<PsiLanguageInjectionHost> injectionHosts = PsiTreeUtil.findChildrenOfType(variableProvider,
                    PsiLanguageInjectionHost.class);
            injectionHosts.stream()
                    .filter(YAMLValue.class::isInstance)
                    .forEach(host -> addReferencesFromInjectedLanguage(element, (YAMLValue) host, consumer));
        }
        return true;
    }

    private void addReferencesFromInjectedLanguage(PsiElement target,
                                                   YAMLValue value,
                                                   @NotNull Processor<? super PsiReference> consumer) {
        final Collection<PsiVariable> variables = OMTProviderUtil.getInjectedContent(value, PsiVariable.class);
        variables.stream()
                .map(PsiElement::getReference)
                .filter(Objects::nonNull)
                .filter(psiReference -> psiReference.isReferenceTo(target))
                .forEach(consumer::process);
    }

    private YAMLMapping getVariableProvider(PsiElement element) {
        final LinkedHashMap<YAMLMapping, OMTVariableProvider> yamlMappingOMTVariableProviderLinkedHashMap = OMTMetaTreeUtil.collectMetaParents(
                element,
                YAMLMapping.class,
                OMTVariableProvider.class,
                false,
                Objects::isNull);
        return yamlMappingOMTVariableProviderLinkedHashMap.keySet().stream().findFirst().orElse(null);
    }

}
