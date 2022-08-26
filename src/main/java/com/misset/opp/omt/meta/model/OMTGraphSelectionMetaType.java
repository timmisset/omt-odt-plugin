package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTQueryGraphArrayMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class OMTGraphSelectionMetaType extends OMTMetaType implements OMTDocumented {
    private static final OMTGraphSelectionMetaType INSTANCE = new OMTGraphSelectionMetaType();

    public static OMTGraphSelectionMetaType getInstance() {
        return INSTANCE;
    }

    private static final List<String> additionalDocumentationHeaders = List.of("Discovery of additional graphs", "Server Side Searches", "Live vs Edit");
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("live", OMTQueryGraphArrayMetaType::getInstance);
        features.put("edit", OMTQueryGraphArrayMetaType::getInstance);
    }

    private OMTGraphSelectionMetaType() {
        super("GraphSelection");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public String getDocumentationClass() {
        return "GraphSelection";
    }

    @Override
    public List<String> getAdditionalDescriptionHeaders() {
        return additionalDocumentationHeaders;
    }
}
