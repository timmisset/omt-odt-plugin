package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTPredicateQueryType;
import com.misset.opp.omt.meta.scalars.values.OMTHandlersContextMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTForbiddenPredicatesMetaType extends OMTMetaType implements OMTDocumented {
    private static final OMTForbiddenPredicatesMetaType INSTANCE = new OMTForbiddenPredicatesMetaType();

    public static OMTForbiddenPredicatesMetaType getInstance() {
        return INSTANCE;
    }

    private static final Set<String> requiredFeatures = Set.of("context", "predicates");

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("context", OMTHandlersContextMetaType::getInstance);
        features.put("predicates", OMTPredicateQueryType::getInstance);
    }

    private OMTForbiddenPredicatesMetaType() {
        super("OMT ForbiddenPredicates");
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
        return "ForbiddenPredicates";
    }
}
