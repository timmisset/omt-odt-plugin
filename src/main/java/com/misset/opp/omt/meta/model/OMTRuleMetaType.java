package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.scalars.ODTBooleanQueryType;
import com.misset.opp.omt.meta.model.scalars.ODTQueryMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTRuleMetaType extends OMTMetaType {
    private static final Set<String> requiredFeatures = Set.of("query");
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("query", ODTQueryMetaType::new);
        features.put("strict", ODTBooleanQueryType::new);
    }

    public OMTRuleMetaType() {
        super("OMT Rule");
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