package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.omt.injection.InjectableContentType;

public class OMTExportableQueriesMetaType extends OMTScriptMetaType {
    private static final OMTExportableQueriesMetaType INSTANCE = new OMTExportableQueriesMetaType();

    public static OMTExportableQueriesMetaType getInstance() {
        return INSTANCE;
    }

    private OMTExportableQueriesMetaType() {
        super();
    }

    @Override
    public InjectableContentType getInjectableContentType() {
        return InjectableContentType.QUERY_BLOCK;
    }
}
