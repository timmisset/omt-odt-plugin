package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.arrays.OMTActionsArrayType;
import com.misset.opp.omt.meta.arrays.OMTVariablesArrayType;
import com.misset.opp.omt.meta.arrays.OMTWatchersArrayType;
import com.misset.opp.omt.meta.model.OMTBindingType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionType;
import com.misset.opp.omt.meta.model.OMTPayloadType;
import com.misset.opp.omt.meta.model.OMTPrefixesType;
import com.misset.opp.omt.meta.model.OMTRulesType;
import com.misset.opp.omt.meta.model.scalars.OMTInterpolatedString;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTCommandsType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTQueriesType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptType;
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

public class OMTComponentType extends OMTModelItemDelegate implements OMTVariableProvider {
    protected OMTComponentType() {
        super("OMT Component");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("title", OMTInterpolatedString::new);
        features.put("bindings", OMTBindingType::new);
        features.put("variables", OMTVariablesArrayType::new);
        features.put("graphs", OMTGraphSelectionType::new);
        features.put("watchers", OMTWatchersArrayType::new);
        features.put("prefixes", OMTPrefixesType::new);
        features.put("queries", ODTQueriesType::new);
        features.put("commands", ODTCommandsType::new);
        features.put("onInit", OMTScriptType::new);
        features.put("actions", OMTActionsArrayType::new);
        features.put("payload", OMTPayloadType::new);
        features.put("rules", OMTRulesType::new);
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
}
