package com.misset.opp.omt.meta.providers.util;

import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemDelegateMetaType;
import com.misset.opp.omt.psi.impl.OMTCallableImpl;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.impl.delegate.plaintext.OMTYamlImportMemberDelegate;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.misset.opp.util.CollectionUtil.addToGroupedMap;

/**
 * Util class for classes implementing OMTCallableProvider
 */
public class OMTCallableProviderUtil extends OMTProviderUtil {

    public static void addInjectedCallablesToMap(YAMLMapping mapping,
                                                 String key,
                                                 HashMap<String, List<PsiCallable>> map,
                                                 PsiLanguageInjectionHost host) {
        final YAMLKeyValue callables = mapping.getKeyValueByKey(key);
        if (host != null && PsiTreeUtil.isAncestor(callables, host, true)) {
            // should not return the callables of the host itself
            return;
        }
        if (callables == null || callables.getValue() == null) {
            return;
        }
        getInjectedContent(callables.getValue(), PsiCallable.class)
                .forEach(callable -> addToGroupedMap(callable.getName(), callable, map));
    }

    /**
     * Add import statements to the map with Callables
     * Imports are deferred references that contain references that should resolve to the Callable that is imported
     *
     * @param mapping - the import value YamlMapping
     * @param map     - the map to add the Callable items to
     */
    public static void addImportStatementsToMap(YAMLMapping mapping,
                                                HashMap<String, List<PsiCallable>> map) {
        mapping.getKeyValues().forEach(
                keyValue -> addImportStatementsToMap(keyValue, map)
        );
    }

    private static void addImportStatementsToMap(YAMLKeyValue keyValue,
                                                 HashMap<String, List<PsiCallable>> map) {
        final YAMLValue value = keyValue.getValue();
        if (value instanceof YAMLSequence) {
            final YAMLSequence sequence = (YAMLSequence) value;
            sequence.getItems().stream()
                    .map(YAMLSequenceItem::getValue)
                    .filter(Objects::nonNull)
                    .map(OMTYamlDelegateFactory::createDelegate)
                    .filter(OMTYamlImportMemberDelegate.class::isInstance)
                    .map(OMTYamlImportMemberDelegate.class::cast)
                    .forEach(importMemberDelegate -> {
                        String name = importMemberDelegate.getText();
                        List<PsiCallable> callables = map.getOrDefault(name, new ArrayList<>());
                        callables.add(importMemberDelegate);
                        map.put(name, callables);
                    });
        }
    }

    public static void addModelItemsToMap(YAMLMapping mapping,
                                          HashMap<String, List<PsiCallable>> map) {
        mapping.getKeyValues()
                .forEach(
                        keyValue -> {

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
                                    addToGroupedMap(keyValue.getKeyText(), new OMTCallableImpl(keyValue), map);
                                }
                            }
                        }
                );

    }

}
