package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaInjectable;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTBooleanQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTQueryMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

@SimpleInjectable
public class OMTRuleMetaType extends OMTMetaType implements OMTMetaInjectable {
    private static final OMTRuleMetaType INSTANCE = new OMTRuleMetaType();

    public static OMTRuleMetaType getInstance() {
        return INSTANCE;
    }
    private static final Set<String> requiredFeatures = Set.of("query");
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("query", OMTQueryMetaType::getInstance);
        features.put("strict", OMTBooleanQueryType::getInstance);
    }

    private OMTRuleMetaType() {
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
