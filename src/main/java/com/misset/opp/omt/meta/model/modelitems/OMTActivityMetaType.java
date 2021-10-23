package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.arrays.OMTActionsArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTHandlersArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTVariablesArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTWatchersArrayMetaType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionMetaType;
import com.misset.opp.omt.meta.model.OMTPayloadMetaType;
import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.model.OMTRulesMetaType;
import com.misset.opp.omt.meta.model.scalars.ODTQueryMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTInterpolatedStringMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTReasonMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTCommandsMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTQueriesMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptMetaType;
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

public class OMTActivityMetaType extends OMTModelItemDelegateMetaType implements OMTVariableProvider, OMTCallableProvider {
    protected OMTActivityMetaType() {
        super("OMT Activity");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("title", OMTInterpolatedStringMetaType::new);
        features.put("onDefaultClose", OMTInterpolatedStringMetaType::new);
        features.put("params", OMTParamsArrayMetaType::new);
        features.put("variables", OMTVariablesArrayMetaType::new);
        features.put("handlers", OMTHandlersArrayMetaType::new);
        features.put("graphs", OMTGraphSelectionMetaType::new);
        features.put("watchers", OMTWatchersArrayMetaType::new);
        features.put("rules", OMTRulesMetaType::new);
        features.put("prefixes", OMTPrefixesMetaType::new);
        features.put("queries", ODTQueriesMetaType::new);
        features.put("commands", ODTCommandsMetaType::new);
        features.put("onStart", OMTScriptMetaType::new);
        features.put("onCommit", OMTScriptMetaType::new);
        features.put("onCancel", OMTScriptMetaType::new);
        features.put("onDone", OMTScriptMetaType::new);
        features.put("returns", ODTQueryMetaType::new);
        features.put("actions", OMTActionsArrayMetaType::new);
        features.put("reason", OMTReasonMetaType::new);
        features.put("payload", OMTPayloadMetaType::new);
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
