package com.misset.opp.omt.findUsage;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.intellij.util.QueryExecutor;
import com.misset.opp.callable.psi.PsiNamespacePrefix;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.scalars.OMTIriMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Since we can't control the use-scope of the prefix:iri key:value mapping directly, we should search for the
 * references within this file or inside any injected language fragment
 */
public class OMTPrefixReferencesSearch extends OMTReferencesSearch implements QueryExecutor<PsiReference, ReferencesSearch.SearchParameters> {

    @Override
    public boolean execute(ReferencesSearch.@NotNull SearchParameters queryParameters,
                           @NotNull Processor<? super PsiReference> consumer) {
        return ReadAction.compute(() -> runSearchInReadAction(queryParameters, consumer));
    }

    private boolean runSearchInReadAction(ReferencesSearch.@NotNull SearchParameters queryParameters,
                                          @NotNull Processor<? super PsiReference> consumer) {
        final PsiElement element = queryParameters.getElementToSearch();
        if (!(element instanceof YAMLKeyValue)) {
            return true;
        }

        final OMTMetaTypeProvider metaTypeProvider = OMTMetaTypeProvider.getInstance(element.getProject());
        final YamlMetaType metaType = Optional.ofNullable(metaTypeProvider.getKeyValueMetaType((YAMLKeyValue) element))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .orElse(null);

        if (metaType instanceof OMTIriMetaType) {
            PsiElement scope = element.getContainingFile();
            // references from the same file in the OMT Language
            PsiTreeUtil.findChildrenOfType(scope, YAMLPlainTextImpl.class)
                    .stream()
                    .map(YAMLScalarImpl::getReferences)
                    .flatMap(Stream::of)
                    .filter(reference -> reference.isReferenceTo(element))
                    .forEach(consumer::process);

            addReferencesFromInjectedLanguage(element, scope, PsiNamespacePrefix.class, consumer);
            return false;
        }
        return true;
    }
}
