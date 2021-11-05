package com.misset.opp.omt.meta.providers.util;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OMTProviderUtil {

    /**
     * Returns the injected ODT elements from the YAMLValue.
     * It's assumed the YAMLValue is an implementation of PsiLanguageInjectionHost and the corresponding MetaType provider
     * is in implementation of ODTInjectable.
     * The root element (ODT Script) of the injected ODTFile is searched using the PsiTreeUtil.findChildrenOfType
     */
    public static <T extends PsiElement> Collection<T> getInjectedContent(YAMLValue value, Class<T> contentClass) {
        if(value == null || !value.isValid()) {
            return Collections.emptyList();
        }

        final InjectedLanguageManager instance = InjectedLanguageManager.getInstance(value.getProject());
        final List<Pair<PsiElement, TextRange>> injectedPsiFiles = instance.getInjectedPsiFiles(value);
        return Optional.ofNullable(injectedPsiFiles)
                .map(pairs -> pairs.get(0))
                .map(pair -> pair.getFirst())
                .map(element ->
                                contentClass.isAssignableFrom(element.getClass()) ? Collections.singletonList(contentClass.cast(element)) :
                        PsiTreeUtil.findChildrenOfType(element, contentClass))
                .orElse(Collections.emptyList());
    }

}
