package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTQueryType;
import com.misset.opp.omt.meta.model.scalars.references.OMTPayloadQueryReferenceType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTOnChangeScriptType;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTPayloadItemType extends OMTMetaType {
    private static final Set<String> requiredFeatures = Set.of("value");
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("value", OMTQueryType::new);
        features.put("query", OMTPayloadQueryReferenceType::new);
        features.put("list", () -> new YamlBooleanType("list"));
        features.put("onChange", OMTOnChangeScriptType::new);
    }

    public OMTPayloadItemType() {
        super("OMT PayloadItem");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    protected Set<String> getRequiredFields() {
        return requiredFeatures;
    }
}
