package com.misset.opp.omt.meta.scalars.values;

import java.util.Set;

public class OMTHandlersMetaType extends OMTFixedValueScalarMetaType {

    private static final OMTHandlersMetaType INSTANCE = new OMTHandlersMetaType();

    public static OMTHandlersMetaType getInstance() {
        return INSTANCE;
    }

    private static final Set<String> values = Set.of("create", "delete", "update");

    private OMTHandlersMetaType() {
        super("OMT Type (MergeHandlers)");
    }

    @Override
    Set<String> getAcceptableValues() {
        return values;
    }
}
