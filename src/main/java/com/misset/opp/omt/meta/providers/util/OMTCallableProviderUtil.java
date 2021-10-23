package com.misset.opp.omt.meta.providers.util;

import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.impl.callables.ODTDefineStatement;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.modelitems.OMTActivityType;
import com.misset.opp.omt.meta.model.modelitems.OMTProcedureType;
import com.misset.opp.omt.meta.model.modelitems.OMTStandaloneQueryType;
import com.misset.opp.omt.meta.model.modelitems.ontology.OMTOntologyType;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.List;

import static com.misset.opp.util.CollectionUtil.addToGroupedMap;

/**
 * Util class for classes implementing OMTCallableProvider
 */
public class OMTCallableProviderUtil extends OMTProviderUtil {

    public static void addDefinedStatementsToMap(YAMLMapping mapping,
                                                 String key,
                                                 HashMap<String, List<PsiElement>> map) {
        final YAMLKeyValue callables = mapping.getKeyValueByKey(key);
        if (callables == null || callables.getValue() == null) {
            return;
        }
        getInjectedContent(callables.getValue(), ODTDefineStatement.class)
                .forEach(odtDefineStatement -> addToGroupedMap(odtDefineStatement.getName(), odtDefineStatement.getDefineName(), map));
    }

    public static void addModelItemsToMap(YAMLMapping mapping,
                                          HashMap<String, List<PsiElement>> map) {
        mapping.getKeyValues().forEach(
                keyValue -> {
                    final YAMLValue value = keyValue.getValue();
                    final YamlMetaTypeProvider.MetaTypeProxy valueMetaType = OMTMetaTypeProvider.getInstance(value.getProject())
                            .getValueMetaType(value);
                    if(valueMetaType != null) {
                        final YamlMetaType metaType = valueMetaType.getMetaType();
                        if(metaType instanceof OMTActivityType || metaType instanceof OMTProcedureType) {
                            addToGroupedMap("@" + keyValue.getKeyText(), keyValue, map);
                        } else if(metaType instanceof OMTStandaloneQueryType || metaType instanceof OMTOntologyType) {
                            addToGroupedMap(keyValue.getKeyText(), keyValue, map);
                        }
                    }

                }
        );

    }

}
