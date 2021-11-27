package com.misset.opp.omt.meta.providers.util;

import com.intellij.psi.PsiElement;
import com.misset.opp.callable.psi.PsiCallable;
import com.misset.opp.omt.meta.OMTImportMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.modelitems.OMTActivityMetaType;
import com.misset.opp.omt.meta.model.modelitems.OMTProcedureMetaType;
import com.misset.opp.omt.meta.model.modelitems.OMTStandaloneQueryMetaType;
import com.misset.opp.omt.meta.model.modelitems.ontology.OMTOntologyMetaType;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;

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
                                                 HashMap<String, List<PsiElement>> map) {
        final YAMLKeyValue callables = mapping.getKeyValueByKey(key);
        if (callables == null || callables.getValue() == null) {
            return;
        }
        getInjectedContent(callables.getValue(), PsiCallable.class)
                .forEach(callable -> addToGroupedMap(callable.getCallId(), callable.getCallTarget(), map));
    }

    /**
     * Add import statements to the map with Callables
     * Imports are deferred references that contain references that should resolve to the Callable that is imported
     * @param mapping - the import value YamlMapping
     * @param map - the map to add the Callable items to
     */
    public static void addImportStatementsToMap(YAMLMapping mapping,
                                                HashMap<String, List<PsiElement>> map) {
        mapping.getKeyValues().forEach(
                keyValue -> addImportStatementsToMap(keyValue, map)
        );
    }
    private static void addImportStatementsToMap(YAMLKeyValue keyValue,
                                                 HashMap<String, List<PsiElement>> map) {
        final YAMLValue value = keyValue.getValue();
        final HashMap<String, List<PsiElement>> exportingMembersMap = OMTImportMetaType.getExportedMembersFromOMTFile(
                keyValue);
        if(value instanceof YAMLSequence) {
            final YAMLSequence sequence = (YAMLSequence) value;
            sequence.getItems().stream()
                    .map(YAMLSequenceItem::getValue)
                    .filter(Objects::nonNull)
                    .map(PsiElement::getText)
                    .forEach(s -> addExportingMemberToMap(s, exportingMembersMap, map));
        }
    }
    private static void addExportingMemberToMap(String name, HashMap<String, List<PsiElement>> exportingMembersMap, HashMap<String, List<PsiElement>> map) {
        String key = exportingMembersMap.containsKey(name) ? name : "@" + name;
        if(!exportingMembersMap.containsKey(key)) { return; }
        map.put(key, exportingMembersMap.get(key));
    }

    public static void addModelItemsToMap(YAMLMapping mapping,
                                          HashMap<String, List<PsiElement>> map) {
        mapping.getKeyValues().forEach(
                keyValue -> {
                    final YAMLValue value = keyValue.getValue();
                    if (value == null) {
                        return;
                    }
                    final YamlMetaTypeProvider.MetaTypeProxy valueMetaType = OMTMetaTypeProvider.getInstance(value.getProject())
                            .getValueMetaType(value);
                    if(valueMetaType != null) {
                        final YamlMetaType metaType = valueMetaType.getMetaType();
                        if(metaType instanceof OMTActivityMetaType || metaType instanceof OMTProcedureMetaType) {
                            addToGroupedMap("@" + keyValue.getKeyText(), keyValue, map);
                        } else if(metaType instanceof OMTStandaloneQueryMetaType || metaType instanceof OMTOntologyMetaType) {
                            addToGroupedMap(keyValue.getKeyText(), keyValue, map);
                        }
                    }

                }
        );

    }

}
