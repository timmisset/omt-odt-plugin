package com.misset.opp.omt.meta.scalars.values;

public class OMTLoadableSchemaMetaType extends OMTFileReferenceMetaType {
    private static final OMTLoadableSchemaMetaType INSTANCE = new OMTLoadableSchemaMetaType();

    public static OMTLoadableSchemaMetaType getInstance() {
        return INSTANCE;
    }

    private OMTLoadableSchemaMetaType() {
        super("json");
    }

}
