package com.misset.opp.omt.meta.model.handlers;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.misset.opp.omt.meta.OMTMetaTaggedType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class OMTMergeHandlerMetaType extends OMTMetaTaggedType<YamlMetaType> {
    private static final HashMap<String, Supplier<YamlMetaType>> taggedTypes = new HashMap<>();

    static {
        taggedTypes.put("!ForbiddenPredicates", OMTForbiddenPredicatesMetaType::getInstance);
        taggedTypes.put("!MergePredicates", OMTMergePredicatesMetaType::getInstance);
        taggedTypes.put("!MergeLists", OMTMergeListsMetaType::getInstance);
        taggedTypes.put("!MergeValidation", OMTMergeValidationMetaType::getInstance);
        taggedTypes.put("!OverwriteAll", OMTOverwriteAllMetaType::getInstance);
    }

    @Override
    protected LookupElement getTagLookup(String s) {
        if (s.equals("!OverwriteAll")) {

            return LookupElementBuilder.create(s + " {}").withPresentableText(s.substring(1))
                    .withLookupStrings(List.of(s, s.substring(1)));
        } else {
            return super.getTagLookup(s);
        }
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
