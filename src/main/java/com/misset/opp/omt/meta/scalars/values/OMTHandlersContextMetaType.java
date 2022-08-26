package com.misset.opp.omt.meta.scalars.values;

import java.util.Set;

public class OMTHandlersContextMetaType extends OMTFixedValueScalarMetaType {
    private static final OMTHandlersContextMetaType INSTANCE = new OMTHandlersContextMetaType();

    public static OMTHandlersContextMetaType getInstance() {
        return INSTANCE;
    }

    private static final Set<String> values = Set.of("current", "parent", "both");

    private OMTHandlersContextMetaType() {
        super("OMT Context (MergeHandlers)");
    }

    @Override
    Set<String> getAcceptableValues() {
        return values;
    }
}
