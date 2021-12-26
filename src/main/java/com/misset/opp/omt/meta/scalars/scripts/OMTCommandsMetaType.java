package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.shared.InjectableContentType;

public class OMTCommandsMetaType extends OMTScriptMetaType {
    public OMTCommandsMetaType() {
        super("Script, allocated for Commands");
    }

    public OMTCommandsMetaType(boolean exportable) {
        super("Script, allocated for Commands", exportable);
    }

    @Override
    public InjectableContentType getInjectableContentType() {
        return InjectableContentType.Command;
    }
}
