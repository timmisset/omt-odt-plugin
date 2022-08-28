package com.misset.opp.omt.meta.model.modelitems;

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
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Supplier;

import static com.misset.opp.omt.meta.providers.util.OMTCallableProviderUtil.addInjectedCallablesToMap;

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
    public @NotNull HashMap<String, Collection<PsiVariable>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, Collection<PsiVariable>> variableMap = new HashMap<>();
        OMTVariableProviderUtil.addSequenceToMap(mapping, "variables", variableMap);

        // add the bindings to the map:
        // bindings are not provided as sequence but as a map with a value that can be a shorthand or a destructed notation
        // where the name of the variable that is declared is set using the bindTo property

        final YAMLKeyValue bindings = mapping.getKeyValueByKey("bindings");
        if (bindings != null && bindings.getValue() instanceof YAMLMapping) {
            final YAMLMapping bindingsMap = (YAMLMapping) bindings.getValue();
            bindingsMap
                    .getKeyValues()
                    .stream()
                    .map(YAMLKeyValue::getValue)
                    .forEach(value -> variableMap
                            .computeIfAbsent(OMTVariableProviderUtil.getVariableName(value, "bindTo"), s -> new ArrayList<>())
                            .add(getBindToValue(value)));
        }

        return variableMap;
    }

    private OMTVariable getBindToValue(YAMLValue value) {
        if (value instanceof YAMLMapping) {
            YAMLKeyValue bindTo = ((YAMLMapping) value).getKeyValueByKey("bindTo");
            if (bindTo == null) {
                return null;
            }
            value = bindTo.getValue();
        }
        if (value instanceof YAMLPlainTextImpl) {
            OMTYamlDelegate delegate = OMTYamlDelegateFactory.createDelegate(value);
            if (delegate instanceof OMTVariable) {
                return (OMTVariable) delegate;
            }
        }
        return null;
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
    public @NotNull HashMap<String, Collection<PsiCallable>> getCallableMap(YAMLMapping yamlMapping,
                                                                            PsiLanguageInjectionHost host) {
        HashMap<String, Collection<PsiCallable>> map = new HashMap<>();
        addInjectedCallablesToMap(yamlMapping, "commands", map, host);
        addInjectedCallablesToMap(yamlMapping, "queries", map, host);
        return map;
    }

    @Override
    public String getDocumentationClass() {
        return "Component";
    }
}
