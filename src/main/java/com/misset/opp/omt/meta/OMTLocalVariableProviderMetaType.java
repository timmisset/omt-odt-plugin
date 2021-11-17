package com.misset.opp.omt.meta;

import com.misset.opp.callable.local.LocalVariable;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.omt.meta.providers.OMTLocalVariableProvider;
import com.misset.opp.omt.meta.providers.OMTLocalVariableTypeProvider;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Provides LocalVariables present at the given position in the OMTModel
 *
 * @see OMTLocalVariableTypeProviderMetaType provides the types for the variables, which are depend on another
 * level in the model to be calculated
 */
public abstract class OMTLocalVariableProviderMetaType extends OMTScriptMetaType implements OMTLocalVariableProvider {

    protected OMTLocalVariableProviderMetaType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    protected abstract List<String> getLocalVariables();

    @Override
    public Map<String, LocalVariable> getLocalVariableMap(YAMLPsiElement element) {
        final LinkedHashMap<YAMLMapping, OMTLocalVariableTypeProvider> contextProviders = OMTMetaTreeUtil.collectMetaParents(
                element,
                YAMLMapping.class,
                OMTLocalVariableTypeProvider.class,
                false,
                Objects::isNull);

        final HashMap<String, LocalVariable> map = new HashMap<>();
        getLocalVariables().forEach(
                variableName -> map.put(variableName, getLocalVariable(variableName, contextProviders))
        );
        return map;
    }

    private LocalVariable getLocalVariable(String name,
                                           LinkedHashMap<YAMLMapping, OMTLocalVariableTypeProvider> contextProviders) {
        final Set<OntResource> type = contextProviders.entrySet().stream()
                .map(entrySet -> getLocalVariableType(entrySet.getValue(), entrySet.getKey(), name))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(Collections.emptySet());
        return new LocalVariable(name, type);
    }

    /*
        Returns null when the provider doesn't provide for the specific name
     */
    private @Nullable Set<OntResource> getLocalVariableType(OMTLocalVariableTypeProvider provider,
                                                            YAMLMapping mapping,
                                                            String name) {
        if (!provider.providesTypeFor(name)) {
            return null;
        }
        return provider.getType(name, mapping);
    }

}
