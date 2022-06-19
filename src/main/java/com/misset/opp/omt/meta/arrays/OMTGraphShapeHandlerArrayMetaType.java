package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.OMTGraphShapeHandlerMemberMetaType;

public class OMTGraphShapeHandlerArrayMetaType extends OMTSortedArrayMetaType {

    private static final OMTGraphShapeHandlerArrayMetaType INSTANCE = new OMTGraphShapeHandlerArrayMetaType();

    public static OMTGraphShapeHandlerArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTGraphShapeHandlerArrayMetaType() {
        super(OMTGraphShapeHandlerMemberMetaType.getInstance());
    }
}
