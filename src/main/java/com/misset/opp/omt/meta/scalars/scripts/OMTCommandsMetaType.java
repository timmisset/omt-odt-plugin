package com.misset.opp.omt.meta.scalars.scripts;

public class OMTCommandsMetaType extends OMTScriptMetaType {

    private static final OMTCommandsMetaType INSTANCE = new OMTCommandsMetaType();

    public static OMTCommandsMetaType getInstance() {
        return INSTANCE;
    }

    protected OMTCommandsMetaType() {
        super();
    }

}
