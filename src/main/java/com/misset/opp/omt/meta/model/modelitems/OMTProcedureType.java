package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayType;
import com.misset.opp.omt.meta.arrays.OMTVariablesArrayType;
import com.misset.opp.omt.meta.markers.OMTVariableProvider;
import com.misset.opp.omt.meta.model.OMTGraphSelectionType;
import com.misset.opp.omt.meta.model.OMTPrefixesType;
import com.misset.opp.omt.meta.model.handlers.OMTMergeHandlerType;
import com.misset.opp.omt.meta.model.scalars.OMTReasonType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class OMTProcedureType extends OMTMetaType implements OMTVariableProvider {
    protected OMTProcedureType() {
        super("OMT Component");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("params", OMTParamsArrayType::new);
        features.put("variables", OMTVariablesArrayType::new);
        features.put("graphs", OMTGraphSelectionType::new);
        features.put("prefixes", OMTPrefixesType::new);
        features.put("onRun", OMTScriptType::new);
        features.put("handlers", OMTMergeHandlerType::new);
        features.put("reason", OMTReasonType::new);
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
