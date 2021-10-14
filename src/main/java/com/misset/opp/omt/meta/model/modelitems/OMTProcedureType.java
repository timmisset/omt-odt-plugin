package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayType;
import com.misset.opp.omt.meta.arrays.OMTVariablesArrayType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionType;
import com.misset.opp.omt.meta.model.OMTPrefixesType;
import com.misset.opp.omt.meta.model.handlers.OMTMergeHandlerType;
import com.misset.opp.omt.meta.model.scalars.OMTODTScriptType;
import com.misset.opp.omt.meta.model.scalars.OMTReasonType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTProcedureType extends OMTMetaType {
    protected OMTProcedureType() {
        super("OMT Component");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("params", OMTParamsArrayType::new);
        features.put("variables", OMTVariablesArrayType::new);
        features.put("graphs", OMTGraphSelectionType::new);
        features.put("prefixes", OMTPrefixesType::new);
        features.put("onRun", OMTODTScriptType::new);
        features.put("handlers", OMTMergeHandlerType::new);
        features.put("reason", OMTReasonType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
