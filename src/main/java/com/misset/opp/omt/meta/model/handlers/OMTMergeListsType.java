package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.scalars.values.OMTHandlersFromType;
import com.misset.opp.omt.meta.model.scalars.OMTODTBooleanQueryType;
import com.misset.opp.omt.meta.model.scalars.OMTODTQueryType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTMergeListsType extends OMTMetaType {
    private static final Set<String> requiredFeatures = Set.of("subjects", "predicates");

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("subjects", OMTODTQueryType::new);
        features.put("predicates", OMTODTQueryType::new);
        features.put("when", OMTODTBooleanQueryType::new);
        features.put("from", OMTHandlersFromType::new);
    }


    protected OMTMergeListsType() {
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
