package com.misset.opp.omt.meta.model.scalars.scripts;

public class OMTQueriesMetaType extends OMTScriptMetaType {
    public OMTQueriesMetaType() {
        super("Script, allocated for Queries");
    }

    public OMTQueriesMetaType(boolean exportable) {
        super("Script, allocated for Commands", exportable);
    }

}
