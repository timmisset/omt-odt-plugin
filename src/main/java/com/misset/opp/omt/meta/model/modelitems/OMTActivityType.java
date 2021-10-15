package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTActionsArrayType;
import com.misset.opp.omt.meta.arrays.OMTHandlersArrayType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayType;
import com.misset.opp.omt.meta.arrays.OMTVariablesArrayType;
import com.misset.opp.omt.meta.arrays.OMTWatchersArrayType;
import com.misset.opp.omt.meta.markers.OMTVariableProvider;
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
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class OMTActivityType extends OMTMetaType implements OMTVariableProvider {
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
    public HashMap<String, List<YAMLPsiElement>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, List<YAMLPsiElement>> variableMap = new HashMap<>();
        addSequenceToMap(mapping, "variables", variableMap);
        addSequenceToMap(mapping, "params", variableMap);

        return variableMap;
    }

}
