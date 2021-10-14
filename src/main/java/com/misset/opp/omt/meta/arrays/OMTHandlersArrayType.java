package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.handlers.OMTMergeHandlerType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTHandlersArrayType extends YamlArrayType {
    public OMTHandlersArrayType() {
        super(new OMTMergeHandlerType());
    }
}
