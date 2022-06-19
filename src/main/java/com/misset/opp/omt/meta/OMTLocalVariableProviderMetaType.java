package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.providers.OMTLocalVariableProvider;
import com.misset.opp.omt.meta.providers.OMTLocalVariableTypeProvider;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.resolvable.local.LocalVariable;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides LocalVariables present at the given position in the OMTModel.
 * Requires OMTLocalVariableTypeProvider to provide the actual LocalVariables and their types
 * <p>
 * OMTLocalVariableProviderMetaType determines which of the LocalVariables are present at the exact
 * key:value pair in the OMTModel. For example:
 * <p>
 * ```model:
 * Activity: !Activity
 * variables:
 * ## OMTLocalVariableTypeProvider is the entire variable with all keys
 * - name:  $variable
 * value: 'test'
 * onChange: |          <-- OMTLocalVariableProviderMetaType
 * $newValue
 * ```
 * If the Variable would have another script based entry aside from onChange, the local variables would
 * not be present there.
 */
public abstract class OMTLocalVariableProviderMetaType extends OMTScriptMetaType implements OMTLocalVariableProvider {

    protected OMTLocalVariableProviderMetaType() {
        super();
    }

    protected abstract List<String> getLocalVariables(YAMLPsiElement element);

    @Override
    public Map<String, LocalVariable> getLocalVariableMap(YAMLPsiElement element) {
        final LinkedHashMap<YAMLMapping, OMTLocalVariableTypeProvider> contextProviders = OMTMetaTreeUtil.collectMetaParents(
                element,
                YAMLMapping.class,
                OMTLocalVariableTypeProvider.class,
                false,
                Objects::isNull);
        return contextProviders.entrySet().stream()
                .map(entry -> entry.getValue().getLocalVariables(entry.getKey()))
                .flatMap(Collection::stream)
                .filter(localVariable -> getLocalVariables(element).contains(localVariable.getName()))
                .collect(Collectors.toMap(LocalVariable::getName, localVariable -> localVariable, (t, t2) -> t));
    }
}
