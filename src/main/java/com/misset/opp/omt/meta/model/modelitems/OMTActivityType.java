package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.actions.OMTActionsType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionType;
import com.misset.opp.omt.meta.model.OMTPayloadType;
import com.misset.opp.omt.meta.model.OMTPrefixesType;
import com.misset.opp.omt.meta.model.OMTQueryWatcherType;
import com.misset.opp.omt.meta.model.OMTRulesType;
import com.misset.opp.omt.meta.model.handlers.OMTMergeHandlerType;
import com.misset.opp.omt.meta.model.scalars.OMTCommandsType;
import com.misset.opp.omt.meta.model.scalars.OMTInterpolatedString;
import com.misset.opp.omt.meta.model.scalars.OMTODTQueryType;
import com.misset.opp.omt.meta.model.scalars.OMTODTScriptType;
import com.misset.opp.omt.meta.model.scalars.OMTQueriesType;
import com.misset.opp.omt.meta.model.scalars.OMTReasonType;
import com.misset.opp.omt.meta.model.variables.OMTParamType;
import com.misset.opp.omt.meta.model.variables.OMTVariableType;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTActivityType extends OMTMetaType {
    protected OMTActivityType() {
        super("OMT Activity");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("title", OMTInterpolatedString::new);
        features.put("onDefaultClose", OMTInterpolatedString::new);
        features.put("params", () -> new YamlArrayType(new OMTParamType()));
        features.put("variables", () -> new YamlArrayType(new OMTVariableType()));
        features.put("handlers", () -> new YamlArrayType(new OMTMergeHandlerType()));
        features.put("graphs", OMTGraphSelectionType::new);
        features.put("watchers", () -> new YamlArrayType(new OMTQueryWatcherType()));
        features.put("rules", OMTRulesType::new);
        features.put("prefixes", OMTPrefixesType::new);
        features.put("queries", OMTQueriesType::new);
        features.put("commands", OMTCommandsType::new);
        features.put("onStart", OMTODTScriptType::new);
        features.put("onCommit", OMTODTScriptType::new);
        features.put("onCancel", OMTODTScriptType::new);
        features.put("onDone", OMTODTScriptType::new);
        features.put("returns", OMTODTQueryType::new);
        features.put("actions", OMTActionsType::new);
        features.put("reason", OMTReasonType::new);
        features.put("payload", OMTPayloadType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
