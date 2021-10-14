package com.misset.opp.omt.meta.model.scalars.values;

import java.util.Set;

public class OMTHandlersTypeType extends OMTFixedValueScalar {
    private static final Set<String> values = Set.of("create", "delete", "update");
    public OMTHandlersTypeType() {
        super("OMT Type (MergeHandlers)");
    }

    @Override
    Set<String> getAcceptableValues() {
        return values;
    }
}
