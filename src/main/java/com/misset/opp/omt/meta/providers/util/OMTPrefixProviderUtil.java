package com.misset.opp.omt.meta.providers.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.meta.scalars.OMTIriMetaType;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.resolvable.psi.PsiPrefix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.*;

public class OMTPrefixProviderUtil {

    private OMTPrefixProviderUtil() {
        // empty constructor
    }

    public static void addPrefixesToMap(YAMLMapping mapping,
                                        String key,
                                        Map<String, Collection<PsiPrefix>> map) {
        final YAMLKeyValue keyValueByKey = mapping.getKeyValueByKey(key);
        if (keyValueByKey == null) {
            return;
        }

        final YAMLValue value = keyValueByKey.getValue();
        if (value instanceof YAMLMapping) {
            // the prefix block
            // prefix:          <someIri>
            // anotherPrefix:   <anotherIri>

            final YAMLMapping prefixBlock = (YAMLMapping) value;
            prefixBlock.getKeyValues().forEach(
                    prefixDefinition -> {
                        OMTYamlDelegate yamlDelegate = OMTYamlDelegateFactory.createDelegate(prefixDefinition);
                        if (yamlDelegate instanceof PsiPrefix) {
                            map.computeIfAbsent(prefixDefinition.getKeyText(), s -> new ArrayList<>()).add((PsiPrefix) yamlDelegate);
                        }
                    }
            );
        }
    }

    public static String resolveToFullyQualifiedUri(PsiElement element,
                                                    String prefix,
                                                    String localName) {
        final Optional<Collection<PsiPrefix>> resolveResults = OMTMetaTreeUtil.resolveProvider(element,
                OMTPrefixProvider.class,
                prefix,
                OMTPrefixProvider::getPrefixMap);
        if (resolveResults.isPresent()) {
            final ResolveResult[] results = resolveResults.get().stream().map(PsiElementResolveResult::new).toArray(ResolveResult[]::new);
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

    @NotNull
    public static Map<String, Collection<PsiPrefix>> getPrefixMap(YAMLMapping yamlMapping) {
        HashMap<String, Collection<PsiPrefix>> map = new HashMap<>();
        addPrefixesToMap(yamlMapping, "prefixes", map);
        return map;
    }

    public static Map<String, String> getNamespaces(YAMLMapping yamlMapping) {
        final Map<String, Collection<PsiPrefix>> prefixMap = getPrefixMap(yamlMapping);
        HashMap<String, String> namespaces = new HashMap<>();
        prefixMap.forEach((key, psiElements) -> {
            // only namespace should be present per key
            final PsiElement psiElement = psiElements.iterator().next();
            if (psiElement instanceof YAMLKeyValue) {
                final String iri = ((YAMLKeyValue) psiElement).getValueText();
                if (iri.length() > 2) {
                    namespaces.put(iri.substring(1, iri.length() - 1), key);
                }
            }

        });
        return namespaces;
    }
}
