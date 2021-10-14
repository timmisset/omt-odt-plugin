package com.misset.opp.omt.meta.model.scalars.values;

import java.util.Set;

public class OMTHandlersFromType extends OMTFixedValueScalar {
    private static final Set<String> values = Set.of("both", "parent");
    public OMTHandlersFromType() {
        super("OMT From (MergeHandlers)");
    }

    @Override
    Set<String> getAcceptableValues() {
        return values;
    }
}
