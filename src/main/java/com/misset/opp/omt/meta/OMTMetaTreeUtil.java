package com.misset.opp.omt.meta;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.LinkedHashMap;
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
        final OMTMetaTypeProvider omtMetaTypeProvider = new OMTMetaTypeProvider();

        LinkedHashMap<T, U> linkedHashMap = new LinkedHashMap<>();
        PsiTreeUtil.collectParents(element, yamlType, includeMyself, stopCondition)
                .forEach(t -> {
                    final YamlMetaTypeProvider.MetaTypeProxy metaTypeProxy = omtMetaTypeProvider.getMetaTypeProxy(t);
                    if(metaTypeProxy != null && metaTypeOrInterface.isAssignableFrom(metaTypeProxy.getMetaType().getClass())) {
                        linkedHashMap.put(t, metaTypeOrInterface.cast(metaTypeProxy.getMetaType()));
                    }
                });
        return linkedHashMap;
    }

}
