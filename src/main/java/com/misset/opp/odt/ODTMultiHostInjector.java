package com.misset.opp.odt;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.markers.ODTInjectable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class will inject ODT scripts into specific locations in OMT files.
 * Since YAML is a nested language, where most of the document is part of a Value in one of the root elements, only the root elements
 * are provided for injection at the MultiHostInjector
 * <p>
 * Therefore, simply limit the injector to the entire document and process injection for all children that meet the requirements
 */
public class ODTMultiHostInjector implements MultiHostInjector {
    private OMTMetaTypeProvider typeProvider = new OMTMetaTypeProvider();
    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar,
                                     @NotNull PsiElement context) {
        if (!(context instanceof YAMLDocument)) {
            return;
        }
        final YAMLDocument document = (YAMLDocument) context;

        final Collection<PsiLanguageInjectionHost> injectionHostList =
                PsiTreeUtil.findChildrenOfType(document,
                                PsiLanguageInjectionHost.class)
                        .stream()
                        .filter(this::isODTInjectable)
                        .collect(Collectors.toList());
        if (!injectionHostList.isEmpty()) {
            registrar.startInjecting(ODTLanguage.INSTANCE);
            injectionHostList.forEach(host ->
                    registrar.addPlace(null, null, host, getTextRangeInHost(host))
            );
            registrar.doneInjecting();
        }
    }

    private TextRange getTextRangeInHost(PsiLanguageInjectionHost host) {
        if (host instanceof YAMLScalarImpl) {
            // skip any non-value decorator or symbol
            int startOffset = ((YAMLScalarImpl) host).getContentRanges()
                    .stream()
                    .map(TextRange::getStartOffset)
                    .sorted()
                    .findFirst()
                    .orElse(0);
            int endOffset = host.getTextLength();
            return TextRange.create(startOffset, endOffset);
        }
        return TextRange.EMPTY_RANGE;
    }

    /*
        Use the meta-type provider to determine what kind of scalar we are dealing with
     */
    private boolean isODTInjectable(PsiLanguageInjectionHost host) {
        if (!(host instanceof YAMLScalar)) {
            return false;
        }
        final YAMLScalar scalar = (YAMLScalar) host;
        return Optional.ofNullable(typeProvider.getValueMetaType(scalar))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .map(ODTInjectable.class::isInstance)
                .orElse(false);
    }

    @Override
    public @NotNull List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return Collections.singletonList(YAMLDocument.class);
    }
}
