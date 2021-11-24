package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.scalars.queries.ODTQueryMetaType;
import com.misset.opp.omt.meta.model.scalars.values.OMTHandlersContextMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTMergeValidationMetaType extends OMTMetaType {
    private static final Set<String> requiredFeatures = Set.of("context", "query");

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("context", OMTHandlersContextMetaType::new);
        features.put("query", ODTQueryMetaType::new);
    }


    protected OMTMergeValidationMetaType() {
        super("OMT MergePredicates");
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
