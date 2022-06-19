package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.meta.OMTMetaTaggedType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTMergeHandlerMetaType extends OMTMetaTaggedType<YamlMetaType> {
    private static final HashMap<String, Supplier<YamlMetaType>> taggedTypes = new HashMap<>();

    static {
        taggedTypes.put("!MergePredicates", OMTMergePredicatesMetaType::getInstance);
        taggedTypes.put("!MergeLists", OMTMergeListsMetaType::getInstance);
        taggedTypes.put("!MergeValidation", OMTMergeValidationMetaType::getInstance);
        taggedTypes.put("!ForbiddenPredicates", OMTForbiddenPredicatesMetaType::getInstance);
    }

    private static final OMTMergeHandlerMetaType INSTANCE = new OMTMergeHandlerMetaType();

    public static OMTMergeHandlerMetaType getInstance() {
        return INSTANCE;
    }

    private OMTMergeHandlerMetaType() {
        super("OMT MergeHandler");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getTaggedTypes() {
        return taggedTypes;
    }

}
