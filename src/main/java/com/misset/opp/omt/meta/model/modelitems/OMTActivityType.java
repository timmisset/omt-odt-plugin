package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTActionsArrayType;
import com.misset.opp.omt.meta.arrays.OMTHandlersArrayType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayType;
import com.misset.opp.omt.meta.arrays.OMTVariablesArrayType;
import com.misset.opp.omt.meta.arrays.OMTWatchersArrayType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionType;
import com.misset.opp.omt.meta.model.OMTPayloadType;
import com.misset.opp.omt.meta.model.OMTPrefixesType;
import com.misset.opp.omt.meta.model.OMTRulesType;
import com.misset.opp.omt.meta.model.scalars.ODTQueryType;
import com.misset.opp.omt.meta.model.scalars.OMTInterpolatedString;
import com.misset.opp.omt.meta.model.scalars.OMTReasonType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTCommandsType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTQueriesType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptType;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static com.misset.opp.omt.meta.providers.util.OMTCallableProviderUtil.addDefinedStatementsToMap;
import static com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil.addSequenceToMap;

public class OMTActivityType extends OMTModelItemDelegate implements OMTVariableProvider, OMTCallableProvider {
    protected OMTActivityType() {
        super("OMT Activity");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("title", OMTInterpolatedString::new);
        features.put("onDefaultClose", OMTInterpolatedString::new);
        features.put("params", OMTParamsArrayType::new);
        features.put("variables", OMTVariablesArrayType::new);
        features.put("handlers", OMTHandlersArrayType::new);
        features.put("graphs", OMTGraphSelectionType::new);
        features.put("watchers", OMTWatchersArrayType::new);
        features.put("rules", OMTRulesType::new);
        features.put("prefixes", OMTPrefixesType::new);
        features.put("queries", ODTQueriesType::new);
        features.put("commands", ODTCommandsType::new);
        features.put("onStart", OMTScriptType::new);
        features.put("onCommit", OMTScriptType::new);
        features.put("onCancel", OMTScriptType::new);
        features.put("onDone", OMTScriptType::new);
        features.put("returns", ODTQueryType::new);
        features.put("actions", OMTActionsArrayType::new);
        features.put("reason", OMTReasonType::new);
        features.put("payload", OMTPayloadType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, List<PsiElement>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, List<PsiElement>> variableMap = new HashMap<>();
        addSequenceToMap(mapping, "variables", variableMap);
        addSequenceToMap(mapping, "params", variableMap);

        return variableMap;
    }

    @Override
    public @NotNull HashMap<String, List<PsiElement>> getCallableMap(YAMLMapping yamlMapping) {
        HashMap<String, List<PsiElement>> callableMap = new HashMap<>();

        addDefinedStatementsToMap(yamlMapping, "commands", callableMap);
        addDefinedStatementsToMap(yamlMapping, "queries", callableMap);

        return callableMap;
    }

    @Override
    public boolean isCallable() {
        return true;
    }
}
