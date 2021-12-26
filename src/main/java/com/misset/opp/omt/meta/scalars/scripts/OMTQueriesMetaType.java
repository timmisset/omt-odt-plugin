package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.shared.InjectableContentType;

public class OMTQueriesMetaType extends OMTScriptMetaType {
    public OMTQueriesMetaType() {
        super("Script, allocated for Queries");
    }

    public OMTQueriesMetaType(boolean exportable) {
        super("Script, allocated for Queries", exportable);
    }

    @Override
    public InjectableContentType getInjectableContentType() {
        return InjectableContentType.Query;
    }
}
