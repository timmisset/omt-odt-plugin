package com.misset.opp.odt;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.shared.InjectionHost;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLDocument;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The ODTMultiHostInjector will inject ODT language on any YamlMetaType that implements OMTInjectable
 * The OMTMetaTypeProvider contains the entire OMT structure and has specific Scalar types that are recognized to be injectable
 */
public class ODTMultiHostInjector implements MultiHostInjector {
    private static final Logger LOGGER = Logger.getInstance(ODTMultiHostInjector.class);

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar,
                                     @NotNull PsiElement context) {
        LoggerUtil.runWithLogger(LOGGER, "getLanguagesToInject", () -> {
            if (!(context instanceof YAMLDocument)) {
                return;
            }
            final YAMLDocument document = (YAMLDocument) context;

            PsiTreeUtil.findChildrenOfType(document,
                            InjectionHost.class)
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(injectionHost -> inject(registrar, injectionHost));
        });
    }

    private void inject(MultiHostRegistrar registrar,
                        InjectionHost injectionHost) {
        List<TextRange> textRanges = LoggerUtil.computeWithLogger(LOGGER, "getTextRanges", injectionHost::getTextRanges);
        if (textRanges.isEmpty()) {
            return;
        }

        LoggerUtil.runWithLogger(LOGGER, "doInjection", () -> {
            registrar.startInjecting(ODTLanguage.INSTANCE);
            for (TextRange textRange : textRanges) {
                registrar.addPlace(injectionHost.getPrefix(), injectionHost.getSuffix(), injectionHost, textRange);
            }
            registrar.doneInjecting();
        });
    }

    @Override
    public @NotNull List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return Collections.singletonList(YAMLDocument.class);
    }

}
