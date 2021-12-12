package com.misset.opp.odt;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

public class ODTInjectionUtil {

    /**
     * Returns the InjectionHost which is the host for the entire ODT file that the provided element is part of
     */
    public static PsiLanguageInjectionHost getInjectionHost(PsiElement element) {
        return Optional.ofNullable(element)
                .map(PsiElement::getContainingFile)
                .filter(ODTFile.class::isInstance)
                .map(ODTFile.class::cast)
                .map(ODTFile::getHost)
                .orElse(null);
    }

    public static YamlMetaType getInjectionMetaType(PsiElement element) {
        return Optional.ofNullable(getInjectionHost(element))
                .filter(YAMLValue.class::isInstance)
                .map(YAMLValue.class::cast)
                .map(yamlPsiElement -> OMTMetaTypeProvider.getInstance(element.getProject())
                        .getValueMetaType(yamlPsiElement))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .orElse(null);
    }

    public static <T extends YAMLPsiElement, U> LinkedHashMap<T, U> getProviders(PsiElement element,
                                                                                 Class<T> yamlClass,
                                                                                 Class<U> metaTypeOrInterface) {
        return Optional.ofNullable(getInjectionHost(element))
                .map(host -> OMTMetaTreeUtil.collectMetaParents(
                        host, yamlClass, metaTypeOrInterface, true, Objects::isNull
                ))
                .orElse(new LinkedHashMap<>());
    }
}
