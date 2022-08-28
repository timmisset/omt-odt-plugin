package com.misset.opp.omt.meta.providers.util;

import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.injection.OMTODTInjectionUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemDelegateMetaType;
import com.misset.opp.omt.psi.impl.OMTCallableImpl;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlImportMemberDelegate;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.*;

import java.util.*;

/**
 * Util class for classes implementing OMTCallableProvider
 */
public class OMTCallableProviderUtil {

    public static void addInjectedCallablesToMap(YAMLMapping mapping,
                                                 String key,
                                                 Map<String, Collection<PsiCallable>> map,
                                                 PsiLanguageInjectionHost host) {
        final YAMLKeyValue callables = mapping.getKeyValueByKey(key);
        if (host != null && PsiTreeUtil.isAncestor(callables, host, true)) {
            // should not return the callables of the host itself
            return;
        }
        if (callables == null || callables.getValue() == null) {
            return;
        }
        OMTODTInjectionUtil.getInjectedContent(callables.getValue(), PsiCallable.class)
                .forEach(callable -> map.computeIfAbsent(callable.getName(), s -> new ArrayList<>()).add(callable));
    }

    /**
     * Add import statements to the map with Callables
     * Imports are deferred references that contain references that should resolve to the Callable that is imported
     *
     * @param mapping - the import value YamlMapping
     * @param map     - the map to add the Callable items to
     */
    public static void addImportStatementsToMap(YAMLMapping mapping,
                                                HashMap<String, Collection<PsiCallable>> map) {
        mapping.getKeyValues().forEach(
                keyValue -> addImportStatementsToMap(keyValue, map)
        );
    }

    private static void addImportStatementsToMap(YAMLKeyValue keyValue,
                                                 HashMap<String, Collection<PsiCallable>> map) {
        final YAMLValue value = keyValue.getValue();
        if (value instanceof YAMLSequence) {
            final YAMLSequence sequence = (YAMLSequence) value;
            sequence.getItems().stream()
                    .map(YAMLSequenceItem::getValue)
                    .filter(Objects::nonNull)
                    .map(OMTYamlDelegateFactory::createDelegate)
                    .filter(OMTYamlImportMemberDelegate.class::isInstance)
                    .map(OMTYamlImportMemberDelegate.class::cast)
                    .forEach(importMemberDelegate ->
                            map.computeIfAbsent(importMemberDelegate.getText(), s -> new ArrayList<>()).add(importMemberDelegate));
        }
    }

    public static void addModelItemsToMap(YAMLMapping mapping,
                                          Map<String, Collection<PsiCallable>> map) {
        mapping.getKeyValues().forEach(keyValue -> addModelItemToMap(map, keyValue));
    }

    public static void addModelItemToMap(Map<String, Collection<PsiCallable>> map, YAMLKeyValue keyValue) {
        final YAMLValue value = keyValue.getValue();
        if (value == null) {
            return;
        }
        final YamlMetaTypeProvider.MetaTypeProxy valueMetaType = OMTMetaTypeProvider.getInstance(value.getProject())
                .getValueMetaType(value);
        if (valueMetaType != null) {
            final YamlMetaType metaType = valueMetaType.getMetaType();
            if (metaType instanceof OMTModelItemDelegateMetaType &&
                    ((OMTModelItemDelegateMetaType) metaType).isCallable()) {
                map.computeIfAbsent(keyValue.getKeyText(), s -> new ArrayList<>()).add(new OMTCallableImpl(keyValue));
            }
        }
    }

}
