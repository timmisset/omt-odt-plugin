package com.misset.opp.omt.meta.scalars.scripts;

public class OMTExportableCommandsMetaType extends OMTCommandsMetaType {

    private static final OMTExportableCommandsMetaType INSTANCE = new OMTExportableCommandsMetaType();

    public static OMTExportableCommandsMetaType getInstance() {
        return INSTANCE;
    }

    private OMTExportableCommandsMetaType() {
        super();
    }

    @Override
    public boolean isExportable() {
        return true;
    }
}
