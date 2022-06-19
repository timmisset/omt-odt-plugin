package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTBooleanQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTPredicateQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTSubjectQueryType;
import com.misset.opp.omt.meta.scalars.values.OMTHandlersFromMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTMergeListsMetaType extends OMTMetaType implements OMTDocumented {
    private static final OMTMergeListsMetaType INSTANCE = new OMTMergeListsMetaType();

    public static OMTMergeListsMetaType getInstance() {
        return INSTANCE;
    }

    private static final Set<String> requiredFeatures = Set.of("subjects", "predicates");

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("subjects", OMTSubjectQueryType::getInstance);
        features.put("predicates", OMTPredicateQueryType::getInstance);
        features.put("when", OMTBooleanQueryType::getInstance);
        features.put("from", OMTHandlersFromMetaType::getInstance);
    }

    private OMTMergeListsMetaType() {
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
        return "MergeLists";
    }
}
