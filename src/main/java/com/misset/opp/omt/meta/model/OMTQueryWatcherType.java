package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.scalars.ODTQueryType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTOnChangeScriptType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTQueryWatcherType extends OMTMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("query", ODTQueryType::new);
        features.put("onChange", ODTOnChangeScriptType::new);
    }

    public OMTQueryWatcherType() {
        super("OMT QueryWatcher");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

}
