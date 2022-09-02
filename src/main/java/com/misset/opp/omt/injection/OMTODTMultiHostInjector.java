package com.misset.opp.omt.injection;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.omt.meta.OMTMetaInjectable;
import com.misset.opp.omt.psi.impl.yaml.YAMLOMTQuotedStringImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.impl.YAMLScalarImpl;

import java.util.Collections;
import java.util.List;

/**
 * The ODTMultiHostInjector will inject ODT language on any YamlMetaType that implements OMTInjectable
 * The OMTMetaTypeProvider contains the entire OMT structure and has specific Scalar types that are recognized to be injectable
 */
public class OMTODTMultiHostInjector implements MultiHostInjector {
    @Override
    public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar,
                                     @NotNull PsiElement context) {
        YamlMetaType injectionMetaType = OMTODTInjectionUtil.getInjectionMetaType(context);
        if (injectionMetaType instanceof OMTMetaInjectable && context instanceof YAMLScalarImpl) {
            inject(registrar, (PsiLanguageInjectionHost) context, (OMTMetaInjectable) injectionMetaType);
        }
    }

    private void inject(MultiHostRegistrar registrar,
                        PsiLanguageInjectionHost context,
                        OMTMetaInjectable metaInjectable) {
        List<TextRange> textRanges = metaInjectable.getTextRanges(context);
        if (textRanges.isEmpty()) {
            return;
        }

        registrar.startInjecting(OMTODTFragmentLanguage.INSTANCE);
        String prefix = null;
        String suffix = null;
        if (context instanceof YAMLOMTQuotedStringImpl) {
            prefix = context.getText().substring(0, 1);
            suffix = context.getText().substring(context.getTextLength() - 1);
        }

        for (TextRange textRange : textRanges) {
            registrar.addPlace(prefix, suffix, context, textRange);
        }
        registrar.doneInjecting();
    }

    @Override
    public @NotNull List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
        return Collections.singletonList(PsiLanguageInjectionHost.class);
    }
}
