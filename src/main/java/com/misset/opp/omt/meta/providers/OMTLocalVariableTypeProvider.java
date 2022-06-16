package com.misset.opp.omt.meta.providers;

import com.intellij.openapi.util.Key;
import com.intellij.psi.util.CachedValue;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.local.LocalVariable;
import com.misset.opp.resolvable.psi.PsiResolvableQuery;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.*;

/**
 * Whenever a LocalVariable is available, the (usually) onChange script makes the variables available
 * However, it doesn't know the context it gets created in. This is provided by, for example, resolving the value or query
 * in which case the $newValue and $oldValue will receive those types
 */
public interface OMTLocalVariableTypeProvider extends OMTMetaTypeStructureProvider {
    Key<CachedValue<LinkedHashMap<YAMLMapping, OMTLocalVariableTypeProvider>>> KEY = new Key<>(
            "OMTLocalVariableTypeProvider");

    List<LocalVariable> getLocalVariables(YAMLMapping mapping);

    YAMLValue getTypeProviderMap(YAMLMapping mapping);

    default Set<OntResource> getType(YAMLMapping mapping) {
        final YAMLValue yamlValue = getTypeProviderMap(mapping);
        return Optional.ofNullable(yamlValue)
                .map(value -> OMTProviderUtil.getInjectedContent(value, PsiResolvableQuery.class))
                .orElse(Collections.emptySet())
                .stream()
                .map(Resolvable::resolve)
                .findFirst()
                .orElse(Collections.emptySet());
    }
}
