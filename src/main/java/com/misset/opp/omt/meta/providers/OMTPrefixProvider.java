package com.misset.opp.omt.meta.providers;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;

import static com.misset.opp.omt.meta.providers.util.OMTPrefixProviderUtil.addPrefixesToMap;

public interface OMTPrefixProvider {
    @NotNull
    /**
     * Returns the prefix map from the expected key 'prefixes', since all current providers
     * use the same key and structure to provide the structure, this interface has a default method
     */
    default HashMap<String, List<PsiElement>> getPrefixMap(YAMLMapping yamlMapping) {
            HashMap<String, List<PsiElement>> map = new HashMap<>();
            addPrefixesToMap(yamlMapping, "prefixes", map);
            return map;
    }
}
