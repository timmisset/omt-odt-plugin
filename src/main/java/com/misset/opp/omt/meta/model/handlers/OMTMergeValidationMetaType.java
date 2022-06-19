package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTHandlersContextMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTMergeValidationMetaType extends OMTMetaType implements OMTDocumented {

    private static final OMTMergeValidationMetaType INSTANCE = new OMTMergeValidationMetaType();

    public static OMTMergeValidationMetaType getInstance() {
        return INSTANCE;
    }

    private static final Set<String> requiredFeatures = Set.of("context", "query");

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("context", OMTHandlersContextMetaType::getInstance);
        features.put("query", OMTQueryMetaType::getInstance);
    }

    private OMTMergeValidationMetaType() {
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

    @Override
    public String getDocumentationClass() {
        return "MergeValidation";
    }
}
