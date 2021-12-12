package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.arrays.OMTVariablesArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTWatchersArrayMetaType;
import com.misset.opp.omt.meta.model.*;
import com.misset.opp.omt.meta.model.scalars.OMTInterpolatedStringMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTCommandsMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTQueriesMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static com.misset.opp.util.CollectionUtil.addToGroupedMap;

public class OMTComponentMetaType extends OMTModelItemDelegateMetaType implements OMTVariableProvider, OMTPrefixProvider {
    protected OMTComponentMetaType() {
        super("OMT Component");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("title", OMTInterpolatedStringMetaType::new);
        features.put("bindings", OMTBindingMetaType::new);
        features.put("variables", OMTVariablesArrayMetaType::new);
        features.put("graphs", OMTGraphSelectionMetaType::new);
        features.put("watchers", OMTWatchersArrayMetaType::new);
        features.put("prefixes", OMTPrefixesMetaType::new);
        features.put("queries", OMTQueriesMetaType::new);
        features.put("commands", OMTCommandsMetaType::new);
        features.put("onInit", OMTScriptMetaType::new);
        features.put("actions", OMTActionsMetaType::new);
        features.put("payload", OMTPayloadMetaType::new);
        features.put("rules", OMTRulesMetaType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, List<PsiElement>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, List<PsiElement>> variableMap = new HashMap<>();
        OMTVariableProviderUtil.addSequenceToMap(mapping, "variables", variableMap);

        // add the bindings to the map:
        // bindings are not provided as sequence but as a map with a value that can be a shorthand or a destructed notation
        // where the name of the variable that is declared is set using the bindTo property

        final YAMLKeyValue bindings = mapping.getKeyValueByKey("bindings");
        if(bindings != null && bindings.getValue() instanceof YAMLMapping) {
            final YAMLMapping bindingsMap = (YAMLMapping) bindings.getValue();
            bindingsMap
                    .getKeyValues()
                    .stream()
                    .map(YAMLKeyValue::getValue)
                    .forEach(value -> addToGroupedMap(OMTVariableProviderUtil.getVariableName(value, "bindTo"), value, variableMap));
        }

        return variableMap;
    }

    @Override
    public boolean isCallable() {
        return false;
    }

    @Override
    public String getType() {
        return "Component";
    }
}
