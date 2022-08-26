package com.misset.opp.omt.meta;

import org.jetbrains.yaml.meta.model.YamlStringType;

public class OMTGraphShapeHandlerMemberMetaType extends YamlStringType {
    private static final OMTGraphShapeHandlerMemberMetaType INSTANCE = new OMTGraphShapeHandlerMemberMetaType();

    public static OMTGraphShapeHandlerMemberMetaType getInstance() {
        return INSTANCE;
    }

    private OMTGraphShapeHandlerMemberMetaType() {
    }
}
