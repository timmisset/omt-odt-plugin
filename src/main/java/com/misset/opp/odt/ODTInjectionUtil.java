package com.misset.opp.odt;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.ResolveResult;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

public class ODTInjectionUtil {

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

    public static <T extends YAMLPsiElement, U> LinkedHashMap<T, U> getProviders(PsiElement element,
                                                                                 Class<T> yamlClass,
                                                                                 Class<U> metaTypeOrInterface) {
        return Optional.ofNullable(getInjectionHost(element))
                .map(host -> OMTMetaTreeUtil.collectMetaParents(
                        host, yamlClass, metaTypeOrInterface, false, Objects::isNull
                ))
                .orElse(new LinkedHashMap<>());
    }
}
