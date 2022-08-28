package com.misset.opp.omt.meta.providers;

import com.intellij.psi.PsiElement;
import com.misset.opp.resolvable.psi.PsiPrefix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.misset.opp.omt.meta.providers.util.OMTPrefixProviderUtil.addPrefixesToMap;

public interface OMTPrefixProvider extends OMTMetaTypeStructureProvider {
    /**
     * Returns the prefix map from the expected key 'prefixes', since all current providers
     * use the same key and structure to provide the structure, this interface has a default method
     */
    default @NotNull Map<String, Collection<PsiPrefix>> getPrefixMap(YAMLMapping yamlMapping) {
        HashMap<String, Collection<PsiPrefix>> map = new HashMap<>();
        addPrefixesToMap(yamlMapping, "prefixes", map);
        return map;
    }

    /**
     * Returns a map with the namespace as key and the prefix as value
     * Use this to create suggestions for creating curies from fully qualified URIs
     */
    @NotNull
    default Map<String, String> getNamespaces(YAMLMapping yamlMapping) {
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
