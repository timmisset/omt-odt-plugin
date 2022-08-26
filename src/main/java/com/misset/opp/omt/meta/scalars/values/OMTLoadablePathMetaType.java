package com.misset.opp.omt.meta.scalars.values;

public class OMTLoadablePathMetaType extends OMTFileReferenceMetaType {
    private static final OMTLoadablePathMetaType INSTANCE = new OMTLoadablePathMetaType();

    public static OMTLoadablePathMetaType getInstance() {
        return INSTANCE;
    }

    private OMTLoadablePathMetaType() {
        super("json");
    }
}
