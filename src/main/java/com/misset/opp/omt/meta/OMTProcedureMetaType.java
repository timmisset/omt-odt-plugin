package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionMetaType;
import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTProcedureMetaType extends OMTMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("params", OMTParamsArrayMetaType::getInstance);
        features.put("graphs", OMTGraphSelectionMetaType::getInstance);
        features.put("prefixes", OMTPrefixesMetaType::getInstance);
        features.put("onRequest", OMTScriptMetaType::getInstance);
    }

    private static final OMTProcedureMetaType INSTANCE = new OMTProcedureMetaType();

    public static OMTProcedureMetaType getInstance() {
        return INSTANCE;
    }

    private OMTProcedureMetaType() {
        super("OMT Service");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

}
