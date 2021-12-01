package com.misset.opp.omt.meta.providers;

import com.intellij.openapi.util.Key;
import com.intellij.psi.util.CachedValue;
import com.misset.opp.callable.local.LocalVariableTypeProvider;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Whenever a LocalVariable is available, the (usually) onChange script makes the variables available
 * However, it doesn't know the context it gets created in. This is provided by, for example, resolving the value or query
 * in which case the $newValue and $oldValue will receive those types
 */
public interface OMTLocalVariableTypeProvider extends LocalVariableTypeProvider, OMTMetaTypeStructureProvider {
    Key<CachedValue<LinkedHashMap<YAMLPsiElement, OMTLocalVariableTypeProvider>>> KEY = new Key<>(
            "OMTLocalVariableTypeProvider");

    Set<OntResource> getType(String name,
                             YAMLMapping mapping);
}
