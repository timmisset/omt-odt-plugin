package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTQueryGraphArrayMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTGraphSelectionMetaType extends OMTMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("live", OMTQueryGraphArrayMetaType::new);
        features.put("edit", OMTQueryGraphArrayMetaType::new);
    }

    public OMTGraphSelectionMetaType() {
        super("GraphSelection");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
