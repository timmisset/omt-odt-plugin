package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.handlers.OMTMergeHandlerMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTHandlersArrayMetaType extends YamlArrayType {
    private static final OMTHandlersArrayMetaType INSTANCE = new OMTHandlersArrayMetaType();

    public static OMTHandlersArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTHandlersArrayMetaType() {
        super(OMTMergeHandlerMetaType.getInstance());
    }
}
