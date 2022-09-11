package com.misset.opp.omt.meta.providers;

import com.misset.opp.omt.commands.LocalVariable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.List;
import java.util.Set;

/**
 * Whenever a LocalVariable is available, the (usually) onChange script makes the variables available
 * However, it doesn't know the context it gets created in. This is provided by, for example, resolving the value or query
 * in which case the $newValue and $oldValue will receive those types
 */
public interface OMTLocalVariableTypeProvider {
    List<LocalVariable> getLocalVariables(YAMLMapping mapping);

    YAMLValue getTypeProviderMap(YAMLMapping mapping);

    Set<OntResource> getType(YAMLMapping mapping);
}
