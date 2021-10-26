package com.misset.opp.omt.meta.providers.util;

import com.intellij.psi.PsiElement;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.List;

import static com.misset.opp.util.CollectionUtil.addToGroupedMap;

public class OMTPrefixProviderUtil extends OMTProviderUtil {

    public static void addPrefixesToMap(YAMLMapping mapping,
                                                 String key,
                                                 HashMap<String, List<PsiElement>> map) {
        final YAMLKeyValue keyValueByKey = mapping.getKeyValueByKey(key);
        if(keyValueByKey == null) {
            return;
        }

        final YAMLValue value = keyValueByKey.getValue();
        if(value instanceof YAMLMapping) {
            // the prefix block
            // prefix:          <someIri>
            // anotherPrefix:   <anotherIri>

            final YAMLMapping prefixBlock = (YAMLMapping) value;
            prefixBlock.getKeyValues().forEach(
                    prefixDefinition -> addToGroupedMap(prefixDefinition.getKeyText(), prefixDefinition, map)
            );
        }

    }

}
