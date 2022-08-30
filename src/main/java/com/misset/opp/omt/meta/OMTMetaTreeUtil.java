package com.misset.opp.omt.meta;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.*;
import java.util.function.BiFunction;

public class OMTMetaTreeUtil {

    private OMTMetaTreeUtil() {
        // empty constructor
    }

    /**
     * Extension build on top the of the PsiTreeUtil to traverse the PsiTree and collect parents based on the resolved
     * meta type of the parents.
     * Returns a linked map (ordered as collected) with the PsiElement as key and the MetaType as value
     */
    public static <T extends YAMLPsiElement, U> Map<T, U> collectMetaParents(PsiElement element,
                                                                             Class<T> yamlClass,
                                                                             Class<U> metaTypeOrInterface) {
        final OMTMetaTypeProvider omtMetaTypeProvider = OMTMetaTypeProvider.getInstance(element.getProject());

        LinkedHashMap<T, U> linkedHashMap = new LinkedHashMap<>();
        PsiTreeUtil.collectParents(element, yamlClass, true, Objects::isNull)
                .forEach(t -> {
                    final YamlMetaTypeProvider.MetaTypeProxy metaTypeProxy = omtMetaTypeProvider.getMetaTypeProxy(t);
                    if (metaTypeProxy != null && metaTypeOrInterface.isAssignableFrom(metaTypeProxy.getMetaType()
                            .getClass())) {
                        linkedHashMap.put(t, metaTypeOrInterface.cast(metaTypeProxy.getMetaType()));
                    }
                });
        return linkedHashMap;
    }

    /**
     * Resolve the provider based on an already available map of providers
     * Use this with a cached resultset of providers
     */
    public static <T, U extends PsiElement> Optional<Collection<U>> resolveProvider(Map<YAMLMapping, T> linkedHashMap,
                                                                                    String key,
                                                                                    BiFunction<T, YAMLMapping, Map<String, Collection<U>>> mapFunction) {
        for (Map.Entry<YAMLMapping, T> entry : linkedHashMap.entrySet()) {
            final Map<String, Collection<U>> map = mapFunction.apply(entry.getValue(), entry.getKey());
            if (map.containsKey(key)) {
                return Optional.ofNullable(map.get(key));
            }
        }

        return Optional.empty();
    }

    /**
     * Walks the OMT PsiTree upwards collecting elements by their meta-type
     */
    public static <T, U extends PsiElement> Optional<Collection<U>> resolveProvider(PsiElement currentElement,
                                                                                    Class<T> providerClass,
                                                                                    String key,
                                                                                    BiFunction<T, YAMLMapping, Map<String, Collection<U>>> mapFunction) {

        final Map<YAMLMapping, T> linkedHashMap = collectMetaParents(currentElement, YAMLMapping.class, providerClass);
        return resolveProvider(linkedHashMap, key, mapFunction);
    }

}
