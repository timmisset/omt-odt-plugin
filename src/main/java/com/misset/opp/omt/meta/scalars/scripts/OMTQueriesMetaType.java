package com.misset.opp.omt.meta.scalars.scripts;

public class OMTQueriesMetaType extends OMTScriptMetaType {
    private static final OMTQueriesMetaType INSTANCE = new OMTQueriesMetaType();

    public static OMTQueriesMetaType getInstance() {
        return INSTANCE;
    }

    private OMTQueriesMetaType() {
        super();
    }

}
