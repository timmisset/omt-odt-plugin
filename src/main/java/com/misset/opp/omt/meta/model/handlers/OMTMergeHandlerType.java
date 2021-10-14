package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.meta.OMTMetaTaggedType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTMergeHandlerType extends OMTMetaTaggedType {
    private static final HashMap<String, Supplier<YamlMetaType>> taggedTypes = new HashMap<>();
    static {
        taggedTypes.put("!MergePredicates", OMTMergePredicatesType::new);
        taggedTypes.put("!MergeLists", OMTMergeListsType::new);
        taggedTypes.put("!MergeValidation", OMTMergeValidationType::new);
    }

    public OMTMergeHandlerType() {
        super("OMT MergeHandler");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getTaggedTypes() {
        return taggedTypes;
    }

}
