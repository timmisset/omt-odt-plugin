package com.misset.opp.omt.meta.providers;

import com.misset.opp.resolvable.psi.PsiPrefix;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collection;
import java.util.Map;

public interface OMTPrefixProvider {
    /**
     * Returns the prefix map from the expected key 'prefixes', since all current providers
     * use the same key and structure to provide the structure, this interface has a default method
     */
    @NotNull Map<String, Collection<PsiPrefix>> getPrefixMap(YAMLMapping yamlMapping);

    /**
     * Returns a map with the namespace as key and the prefix as value
     * Use this to create suggestions for creating curies from fully qualified URIs
     */
    @NotNull Map<String, String> getNamespaces(YAMLMapping yamlMapping);
}
