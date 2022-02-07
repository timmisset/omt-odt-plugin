package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.meta.OMTMetaTaggedType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTMergeHandlerMetaType extends OMTMetaTaggedType<YamlMetaType> {
    private static final HashMap<String, Supplier<YamlMetaType>> taggedTypes = new HashMap<>();
    static {
        taggedTypes.put("!MergePredicates", OMTMergePredicatesMetaType::new);
        taggedTypes.put("!MergeLists", OMTMergeListsMetaType::new);
        taggedTypes.put("!MergeValidation", OMTMergeValidationMetaType::new);
        taggedTypes.put("!ForbiddenPredicates", OMTForbiddenPredicatesMetaType::new);
    }

    public OMTMergeHandlerMetaType() {
        super("OMT MergeHandler");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getTaggedTypes() {
        return taggedTypes;
    }

}
