package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.handlers.OMTMergeHandlerMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTHandlersArrayMetaType extends YamlArrayType {
    public OMTHandlersArrayMetaType() {
        super(new OMTMergeHandlerMetaType());
    }
}
