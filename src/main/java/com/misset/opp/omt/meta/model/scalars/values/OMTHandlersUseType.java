package com.misset.opp.omt.meta.model.scalars.values;

import java.util.Set;

public class OMTHandlersUseType extends OMTFixedValueScalar {
    private static final Set<String> values = Set.of("current", "parent");

    public OMTHandlersUseType() {
        super("OMT From (MergeHandlers)");
    }

    @Override
    Set<String> getAcceptableValues() {
        return values;
    }
}
