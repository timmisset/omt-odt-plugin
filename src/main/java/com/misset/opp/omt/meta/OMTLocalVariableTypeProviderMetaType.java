package com.misset.opp.omt.meta;

import com.misset.opp.callable.Call;
import com.misset.opp.callable.PsiResolvable;
import com.misset.opp.callable.Resolvable;
import com.misset.opp.omt.meta.providers.OMTLocalVariableTypeProvider;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Provides the type for a local variable.
 *
 * @see OMTLocalVariableProviderMetaType provides the actual LocalVariables
 */
public abstract class OMTLocalVariableTypeProviderMetaType extends OMTMetaType implements OMTLocalVariableTypeProvider {

    protected OMTLocalVariableTypeProviderMetaType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    /**
     * Returns a map with variable names and the corresponding YamlValue that needs to be resolved
     * in the injected language to calculate the variable type
     * <p>
     * For example (PayloadItem):
     * myItem:
     * value: 'hi'
     * onChange: $LOG($newValue)
     * <p>
     * The type for $newValue is calculated by resolving the YamlValue at 'value';
     */
    @Nullable
    protected abstract YAMLValue getTypeProviderMap(String variableName,
                                                    YAMLMapping mapping);

    protected abstract List<String> getVariables();

    @Override
    public Set<OntResource> getType(String variableName,
                                    YAMLMapping mapping) {
        final YAMLValue yamlValue = getTypeProviderMap(variableName, mapping);
        return Optional.ofNullable(yamlValue)
                .map(value -> OMTProviderUtil.getInjectedContent(value, PsiResolvable.class))
                .orElse(Collections.emptySet())
                .stream()
                .map(Resolvable::resolve)
                .findFirst()
                .orElse(Collections.emptySet());
    }

    @Override
    public boolean providesTypeFor(String variableName) {
        return getVariables().contains(variableName);
    }

    @Override
    public Set<OntResource> getType(String variableName,
                                    Call call) {
        // Context comes from the OMT model, no call information can be used to determine the type
        return Collections.emptySet();
    }

}
