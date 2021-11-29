package com.misset.opp.omt.meta.providers.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveResult;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.model.scalars.OMTIriMetaType;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    public static String resolveToFullyQualifiedUri(PsiElement element,
                                                    String prefix,
                                                    String localName) {
        final Optional<ResolveResult[]> resolveResults = OMTMetaTreeUtil.resolveProvider(element,
                OMTPrefixProvider.class,
                prefix,
                OMTPrefixProvider::getPrefixMap);
        if (resolveResults.isPresent()) {
            final ResolveResult[] results = resolveResults.get();
            final PsiElement prefixElement = results[0].getElement();
            if (prefixElement instanceof YAMLKeyValue) {
                final String namespace = OMTIriMetaType.getNamespace(((YAMLKeyValue) prefixElement).getValue());
                if (namespace != null) {
                    return namespace + localName;
                }
            }
        }
        return null;
    }

}
