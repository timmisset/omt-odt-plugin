package com.misset.opp.omt.meta.scalars.values;

import java.util.Set;

public class OMTHandlersContextMetaType extends OMTFixedValueScalarMetaType {
    private static final Set<String> values = Set.of("current");
    public OMTHandlersContextMetaType() {
        super("OMT Context (MergeHandlers)");
    }

    @Override
    Set<String> getAcceptableValues() {
        return values;
    }
}
