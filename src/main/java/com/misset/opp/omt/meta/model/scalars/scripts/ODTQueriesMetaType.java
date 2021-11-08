package com.misset.opp.omt.meta.model.scalars.scripts;

public class ODTQueriesMetaType extends OMTScriptMetaType {
    public ODTQueriesMetaType() {
        super("ODT Script (Can only contain Queries)");
    }

    public ODTQueriesMetaType(boolean exportable) {
        super("ODT Script (Can only contain Queries)", exportable);
    }

}
