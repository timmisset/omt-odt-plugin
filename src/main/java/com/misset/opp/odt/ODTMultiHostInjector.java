package com.misset.opp.odt;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLQuotedText;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * The ODTMultiHostInjector will inject ODT language on any YamlMetaType that implements ODTInjectable
 * The OMTMetaTypeProvider contains the entire OMT structure and has specific Scalar types that are recognized to be injectable
 */
public class ODTMultiHostInjector implements MultiHostInjector {
    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar,
                                     @NotNull PsiElement context) {
        if (!(context instanceof YAMLDocument)) {
            return;
        }
        final YAMLDocument document = (YAMLDocument) context;

        PsiTreeUtil.findChildrenOfType(document,
                        PsiLanguageInjectionHost.class)
                .stream()
                .map(this::getODTInjectable)
                .filter(Objects::nonNull)
                .forEach(pair -> inject(registrar, pair.getFirst(), pair.getSecond()));
    }

    private void inject(MultiHostRegistrar registrar,
                        ODTInjectable injectable,
                        YAMLScalarImpl host) {
        final List<TextRange> textRanges = injectable.getTextRanges(host);
        if (textRanges.isEmpty()) {
            return;
        }

        registrar.startInjecting(ODTLanguage.INSTANCE);
        for (TextRange textRange : textRanges) {
            registrar.addPlace(getPrefix(host), getSuffix(host), host, textRange);
        }
        registrar.doneInjecting();
    }

    private String getPrefix(YAMLScalar scalar) {
        /*
            The Quotes are forced, the injection will otherwise trim them, even when the full TextRange is suggested
         */
        if (scalar instanceof YAMLQuotedText) {
            return scalar.getText().substring(0, 1);
        }
        return null;
    }

    private String getSuffix(YAMLScalar scalar) {
        if (scalar instanceof YAMLQuotedText) {
            return scalar.getText().substring(scalar.getTextLength() - 1);
        }
        return null;
    }

    private Pair<ODTInjectable, YAMLScalarImpl> getODTInjectable(PsiLanguageInjectionHost host) {
        if (!(host instanceof YAMLScalarImpl)) {
            return null;
        }
        final YAMLScalarImpl scalar = (YAMLScalarImpl) host;
        return Optional.ofNullable(OMTMetaTypeProvider.getInstance(host.getProject()).getValueMetaType(scalar))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .filter(metaType -> metaType instanceof ODTInjectable)
                .map(ODTInjectable.class::cast)
                .map(metaType -> new Pair<>(metaType, scalar))
                .orElse(null);
    }

    @Override
    public @NotNull List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return Collections.singletonList(YAMLDocument.class);
    }

    /**
     * Returns the YamlScalar which is the host for the entire ODT file that the provided element is part of
     */
    public static YAMLPsiElement getInjectionHost(PsiElement element) {
        return Optional.ofNullable(element)
                .map(PsiElement::getContainingFile)
                .filter(ODTFile.class::isInstance)
                .map(ODTFile.class::cast)
                .map(ODTFile::getHost)
                .orElse(null);
    }

    /**
     * Resolves referencing ODT elements in OMT containers
     */
    public static <T> Optional<ResolveResult[]> resolveInOMT(PsiElement odtElement,
                                                             Class<T> providerClass,
                                                             String key,
                                                             BiFunction<T, YAMLMapping, HashMap<String, List<PsiElement>>> mapFunction) {
        final InjectedLanguageManager languageManager = InjectedLanguageManager.getInstance(odtElement.getProject());
        final PsiLanguageInjectionHost injectionHost = languageManager.getInjectionHost(odtElement);
        if (injectionHost == null) {
            return Optional.empty();
        }

        return OMTMetaTreeUtil.resolveProvider(injectionHost, providerClass, key, mapFunction);
    }

    public static <T extends YAMLPsiElement, U> Optional<Pair<T, U>> getClosestProvider(PsiElement element,
                                                                                        Class<T> yamlClass,
                                                                                        Class<U> metaTypeOrInterface) {
        return Optional.ofNullable(ODTMultiHostInjector.getInjectionHost(element))
                .map(host -> OMTMetaTreeUtil.collectMetaParents(
                        host, yamlClass, metaTypeOrInterface, false, Objects::isNull
                ))
                .map(LinkedHashMap::entrySet)
                .flatMap(ts -> ts.stream().findFirst())
                .map(tuEntry -> new Pair<>(tuEntry.getKey(), tuEntry.getValue()));
    }
}
