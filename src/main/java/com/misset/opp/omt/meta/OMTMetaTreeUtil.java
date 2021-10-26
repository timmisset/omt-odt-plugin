package com.misset.opp.omt.meta;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import com.misset.opp.omt.meta.providers.OMTLocalCommandProvider;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.function.Predicate;

public class OMTMetaTreeUtil {

    /**
     * Extension build on top the of the PsiTreeUtil to traverse the PsiTree and collect parents based on the resolved
     * meta type of the parents.
     * Returns a linked map (ordered as collected) with the PsiElement as key and the MetaType as value
     */
    public static <T extends YAMLPsiElement, U> LinkedHashMap<T, U> collectMetaParents(PsiElement element,
                                                                                       Class<T> yamlType,
                                                                                       Class<U> metaTypeOrInterface,
                                                                                       boolean includeMyself,
                                                                                       Predicate<PsiElement> stopCondition) {
        final OMTMetaTypeProvider omtMetaTypeProvider = OMTMetaTypeProvider.getInstance(element.getProject());

        LinkedHashMap<T, U> linkedHashMap = new LinkedHashMap<>();
        PsiTreeUtil.collectParents(element, yamlType, includeMyself, stopCondition)
                .forEach(t -> {
                    final YamlMetaTypeProvider.MetaTypeProxy metaTypeProxy = omtMetaTypeProvider.getMetaTypeProxy(t);
                    if (metaTypeProxy != null && metaTypeOrInterface.isAssignableFrom(metaTypeProxy.getMetaType()
                            .getClass())) {
                        linkedHashMap.put(t, metaTypeOrInterface.cast(metaTypeProxy.getMetaType()));
                    }
                });
        return linkedHashMap;
    }

    public static LinkedHashMap<YAMLMapping, OMTCallableProvider> collectCallableProviders(PsiElement element) {
        return collectMetaParents(
                element,
                YAMLMapping.class,
                OMTCallableProvider.class,
                false,
                Objects::isNull);
    }

    public static LinkedHashMap<YAMLMapping, OMTLocalCommandProvider> collectLocalCommandProviders(PsiElement element) {
        return collectMetaParents(
                element,
                YAMLMapping.class,
                OMTLocalCommandProvider.class,
                false,
                Objects::isNull);
    }

}
