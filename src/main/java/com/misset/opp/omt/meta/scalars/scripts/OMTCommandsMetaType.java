package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.shared.InjectableContentType;

public class OMTCommandsMetaType extends OMTScriptMetaType {

    private static final OMTCommandsMetaType INSTANCE = new OMTCommandsMetaType();

    public static OMTCommandsMetaType getInstance() {
        return INSTANCE;
    }

    protected OMTCommandsMetaType() {
        super();
    }

    @Override
    public InjectableContentType getInjectableContentType() {
        return InjectableContentType.Command;
    }
}
