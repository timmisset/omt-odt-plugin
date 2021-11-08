package com.misset.opp.omt.meta.model.scalars.scripts;

public class ODTCommandsMetaType extends OMTScriptMetaType {
    public ODTCommandsMetaType() {
        super("ODT Script (Can only contain Commands)");
    }
    public ODTCommandsMetaType(boolean exportable) {
        super("ODT Script (Can only contain Commands)", true);
    }
}
