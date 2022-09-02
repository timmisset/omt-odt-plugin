package com.misset.opp.omt.injection;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OMTODTInjectionUtil {

    private OMTODTInjectionUtil() {
        // empty constructor
    }

    public static PsiLanguageInjectionHost getInjectionHost(PsiElement element) {
        return InjectedLanguageManager.getInstance(element.getProject())
                .getInjectionHost(element);
    }

    @SuppressWarnings("java:S2637")
    public static YamlMetaType getInjectionMetaType(PsiElement element) {
        PsiElement yamlElement = element instanceof YAMLPsiElement ? element : getInjectionHost(element);
        return Optional.ofNullable(yamlElement)
                .filter(YAMLValue.class::isInstance)
                .map(YAMLValue.class::cast)
                .map(yamlPsiElement -> OMTMetaTypeProvider.getInstance(yamlElement.getProject())
                        .getValueMetaType(yamlPsiElement))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .orElse(null);
    }

    /**
     * Returns the injected elements from the YAMLValue.
     * It's assumed the YAMLValue is an implementation of PsiLanguageInjectionHost and the corresponding MetaType provider
     * is in implementation of OMTInjectable.
     * The root element (Script) of the injected File is searched using the PsiTreeUtil.findChildrenOfType
     */
    @SuppressWarnings("java:S1612")
    public static <T extends PsiElement> Collection<T> getInjectedContent(YAMLValue value,
                                                                          Class<T> contentClass) {
        if (value == null || !value.isValid()) {
            return Collections.emptyList();
        }

        final InjectedLanguageManager instance = InjectedLanguageManager.getInstance(value.getProject());
        final List<Pair<PsiElement, TextRange>> injectedPsiFiles = instance.getInjectedPsiFiles(value);
        return Optional.ofNullable(injectedPsiFiles)
                .map(pairs -> pairs.get(0))
                // ignore java S1612 since getFirst is ambiguous in the Pair class
                // https://community.sonarsource.com/t/s1612-should-not-complain-if-method-reference-is-ambiguous/149
                .map(pair -> pair.getFirst())
                .map(element ->
                        contentClass.isAssignableFrom(element.getClass()) ? Collections.singletonList(contentClass.cast(
                                element)) :
                                PsiTreeUtil.findChildrenOfType(element, contentClass))
                .orElse(Collections.emptyList());
    }
}
