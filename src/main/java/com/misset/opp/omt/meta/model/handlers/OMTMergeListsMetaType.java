package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.scalars.queries.ODTBooleanQueryType;
import com.misset.opp.omt.meta.model.scalars.queries.ODTPredicateQueryType;
import com.misset.opp.omt.meta.model.scalars.queries.ODTSubjectQueryType;
import com.misset.opp.omt.meta.model.scalars.values.OMTHandlersFromMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTMergeListsMetaType extends OMTMetaType {
    private static final Set<String> requiredFeatures = Set.of("subjects", "predicates");

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("subjects", ODTSubjectQueryType::new);
        features.put("predicates", ODTPredicateQueryType::new);
        features.put("when", ODTBooleanQueryType::new);
        features.put("from", OMTHandlersFromMetaType::new);
    }


    protected OMTMergeListsMetaType() {
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
