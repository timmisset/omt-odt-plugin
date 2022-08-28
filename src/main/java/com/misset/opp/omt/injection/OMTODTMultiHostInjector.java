package com.misset.opp.omt.injection;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * The ODTMultiHostInjector will inject ODT language on any YamlMetaType that implements OMTInjectable
 * The OMTMetaTypeProvider contains the entire OMT structure and has specific Scalar types that are recognized to be injectable
 */
public class OMTODTMultiHostInjector implements MultiHostInjector {
    private static final Logger LOGGER = Logger.getInstance(OMTODTMultiHostInjector.class);

    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar,
                                     @NotNull PsiElement context) {
        if (context instanceof InjectionHost) {
            inject(registrar, (InjectionHost) context);
        }
    }

    private void inject(MultiHostRegistrar registrar,
                        InjectionHost injectionHost) {
        List<TextRange> textRanges = injectionHost.getTextRanges();
        if (textRanges.isEmpty()) {
            return;
        }

        registrar.startInjecting(OMTODTFragmentLanguage.INSTANCE);
        for (TextRange textRange : textRanges) {
            registrar.addPlace(injectionHost.getPrefix(), injectionHost.getSuffix(), injectionHost, textRange);
        }
        registrar.doneInjecting();
    }

    @Override
    public @NotNull List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return Collections.singletonList(InjectionHost.class);
    }
}
