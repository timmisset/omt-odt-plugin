package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.arrays.OMTVariablesArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTWatchersArrayMetaType;
import com.misset.opp.omt.meta.model.*;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil;
import com.misset.opp.omt.meta.scalars.OMTInterpolatedStringMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTCommandsMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTQueriesMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.misset.opp.omt.meta.providers.util.OMTCallableProviderUtil.addInjectedCallablesToMap;
import static com.misset.opp.util.CollectionUtil.addToGroupedMap;

public class OMTComponentMetaType extends OMTModelItemDelegateMetaType implements
        OMTVariableProvider,
        OMTCallableProvider,
        OMTPrefixProvider,
        OMTDocumented {

    private static final OMTComponentMetaType INSTANCE = new OMTComponentMetaType();

    public static OMTComponentMetaType getInstance() {
        return INSTANCE;
    }

    private OMTComponentMetaType() {
        super("OMT Component");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("title", OMTInterpolatedStringMetaType::getInstance);
        features.put("bindings", OMTBindingMetaType::getInstance);
        features.put("variables", OMTVariablesArrayMetaType::getInstance);
        features.put("graphs", OMTGraphSelectionMetaType::getInstance);
        features.put("watchers", OMTWatchersArrayMetaType::getInstance);
        features.put("prefixes", OMTPrefixesMetaType::getInstance);
        features.put("queries", OMTQueriesMetaType::getInstance);
        features.put("commands", OMTCommandsMetaType::getInstance);
        features.put("onInit", OMTScriptMetaType::getInstance);
        features.put("actions", OMTActionsMapMetaType::getInstance);
        features.put("payload", OMTPayloadMetaType::getInstance);
        features.put("rules", OMTRulesMetaType::getInstance);
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
                    .forEach(value -> addToGroupedMap(OMTVariableProviderUtil.getVariableName(value, "bindTo"), getBindToValue(value), variableMap));
        }

        return variableMap;
    }

    private YAMLValue getBindToValue(YAMLValue value) {
        if (value instanceof YAMLPlainTextImpl) {
            return value;
        }
        return Optional.ofNullable(value)
                .filter(YAMLMapping.class::isInstance)
                .map(YAMLMapping.class::cast)
                .map(mapping -> mapping.getKeyValueByKey("bindTo"))
                .map(YAMLKeyValue::getValue)
                .orElse(null);
    }

    @Override
    public boolean isCallable() {
        return false;
    }

    @Override
    public String getType() {
        return "Component";
    }

    @Override
    public @NotNull HashMap<String, List<PsiCallable>> getCallableMap(YAMLMapping yamlMapping, PsiLanguageInjectionHost host) {
        HashMap<String, List<PsiCallable>> map = new HashMap<>();
        addInjectedCallablesToMap(yamlMapping, "commands", map, host);
        addInjectedCallablesToMap(yamlMapping, "queries", map, host);
        return map;
    }

    @Override
    public String getDocumentationClass() {
        return "Component";
    }
}
