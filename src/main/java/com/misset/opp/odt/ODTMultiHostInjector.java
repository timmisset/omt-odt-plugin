package com.misset.opp.odt;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.scalars.OMTInterpolatedStringMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLQuotedText;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The ODTMultiHostInjector will inject ODT language on any YamlMetaType that implements ODTInjectable
 * The OMTMetaTypeProvider contains the entire OMT structure and has specific Scalar types that are recognized to be injectable
 */
public class ODTMultiHostInjector implements MultiHostInjector {

    private final Pattern INTERPOLATION = Pattern.compile("\\$\\{([^}]+)}");

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar,
                                     @NotNull PsiElement context) {
        if (!(context instanceof YAMLDocument)) {
            return;
        }
        final YAMLDocument document = (YAMLDocument) context;

        // gather all injectable scalars
        final Collection<YAMLScalar> injectionHostList =
                PsiTreeUtil.findChildrenOfType(document,
                                PsiLanguageInjectionHost.class)
                        .stream()
                        .filter(this::isODTInjectable)
                        .map(YAMLScalar.class::cast)
                        .collect(Collectors.toList());
        // start injection
        if (!injectionHostList.isEmpty()) {
            injectionHostList.forEach(host -> {
                if (isInterpolatedString(host)) {
                    registerInterpolatedString(registrar, host);
                } else {
                    registerBlock(registrar, host);
                }
            });
        }
    }

    private boolean isInterpolatedString(YAMLScalar scalar) {
        return Optional.ofNullable(OMTMetaTypeProvider.getInstance(scalar.getProject()).getValueMetaType(scalar))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .map(OMTInterpolatedStringMetaType.class::isInstance)
                .orElse(false);
    }

    private void registerBlock(@NotNull MultiHostRegistrar registrar,
                               YAMLScalar scalar) {
        registrar.startInjecting(ODTLanguage.INSTANCE);
        registrar.addPlace(getPrefix(scalar), getSuffix(scalar), scalar, getTextRangeInHost(scalar));
        registrar.doneInjecting();
    }

    private String getPrefix(YAMLScalar scalar) {
        /*
            The Quotes are forced, the injection will otherwise trim them, even when the full TextRange is suggested
         */
        if(scalar instanceof YAMLQuotedText) { return scalar.getText().substring(0, 1); }
        return null;
    }
    private String getSuffix(YAMLScalar scalar) {
        if(scalar instanceof YAMLQuotedText) { return scalar.getText().substring(scalar.getTextLength() - 1); }
        return null;
    }
    private void registerInterpolatedString(@NotNull MultiHostRegistrar registrar,
                                            YAMLScalar scalar) {
        final Matcher matcher = INTERPOLATION.matcher(scalar.getText());
        boolean b = matcher.find();
        if (!b) {
            return;
        }

        registrar.startInjecting(ODTLanguage.INSTANCE);
        while (b) {
            TextRange textRange = TextRange.create(matcher.start(1), matcher.end(1));
            registrar.addPlace(null, null, scalar, textRange);
            b = matcher.find();
        }
        registrar.doneInjecting();
    }

    private TextRange getTextRangeInHost(PsiLanguageInjectionHost host) {
        if (host instanceof YAMLScalarImpl) {
            // skip any non-value decorator or symbol (such as a multiline decorator)
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

    private boolean isODTInjectable(PsiLanguageInjectionHost host) {
        if (!(host instanceof YAMLScalar)) {
            return false;
        }
        final YAMLScalar scalar = (YAMLScalar) host;
        return Optional.ofNullable(OMTMetaTypeProvider.getInstance(host.getProject()).getValueMetaType(scalar))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .map(metaType -> metaType.getClass().isAnnotationPresent(ODTInjectable.class))
                .orElse(false);
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
}
