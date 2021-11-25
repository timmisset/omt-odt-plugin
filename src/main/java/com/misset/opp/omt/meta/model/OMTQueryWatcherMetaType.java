package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTOnChangeScriptMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTQueryWatcherMetaType extends OMTMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("query", OMTQueryMetaType::new);
        features.put("onChange", OMTOnChangeScriptMetaType::new);
    }

    public OMTQueryWatcherMetaType() {
        super("OMT QueryWatcher");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

}
