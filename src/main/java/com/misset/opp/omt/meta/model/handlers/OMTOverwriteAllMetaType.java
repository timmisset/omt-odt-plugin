package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.documentation.OMTDocumented;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTOverwriteAllMetaType extends OMTMergeMetaType implements OMTDocumented {

    private static final OMTOverwriteAllMetaType INSTANCE = new OMTOverwriteAllMetaType();
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    private OMTOverwriteAllMetaType() {
        super("OMT OverwriteAll");
    }

    public static OMTOverwriteAllMetaType getInstance() {
        return INSTANCE;
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public String getDocumentationClass() {
        return "OverwriteAll";
    }
}
