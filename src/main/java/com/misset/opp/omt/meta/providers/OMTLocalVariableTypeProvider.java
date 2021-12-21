package com.misset.opp.omt.meta.providers;

import com.intellij.openapi.util.Key;
import com.intellij.psi.util.CachedValue;
import com.misset.opp.resolvable.local.LocalVariable;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Whenever a LocalVariable is available, the (usually) onChange script makes the variables available
 * However, it doesn't know the context it gets created in. This is provided by, for example, resolving the value or query
 * in which case the $newValue and $oldValue will receive those types
 */
public interface OMTLocalVariableTypeProvider extends OMTMetaTypeStructureProvider {
    Key<CachedValue<LinkedHashMap<YAMLMapping, OMTLocalVariableTypeProvider>>> KEY = new Key<>(
            "OMTLocalVariableTypeProvider");

    List<LocalVariable> getLocalVariables(YAMLMapping mapping);
}
