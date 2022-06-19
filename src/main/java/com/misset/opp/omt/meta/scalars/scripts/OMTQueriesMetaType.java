package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.shared.InjectableContentType;

public class OMTQueriesMetaType extends OMTScriptMetaType {
    private static final OMTQueriesMetaType INSTANCE = new OMTQueriesMetaType();

    public static OMTQueriesMetaType getInstance() {
        return INSTANCE;
    }

    private OMTQueriesMetaType() {
        super();
    }

    @Override
    public InjectableContentType getInjectableContentType() {
        return InjectableContentType.QueryBlock;
    }
}
