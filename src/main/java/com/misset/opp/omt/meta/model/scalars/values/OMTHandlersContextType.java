package com.misset.opp.omt.meta.model.scalars.values;

import java.util.Set;

public class OMTHandlersContextType extends OMTFixedValueScalar {
    private static final Set<String> values = Set.of("current");
    public OMTHandlersContextType() {
        super("OMT Context (MergeHandlers)");
    }

    @Override
    Set<String> getAcceptableValues() {
        return values;
    }
}
