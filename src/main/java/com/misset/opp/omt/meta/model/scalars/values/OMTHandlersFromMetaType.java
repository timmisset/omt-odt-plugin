package com.misset.opp.omt.meta.model.scalars.values;

import java.util.Set;

public class OMTHandlersFromMetaType extends OMTFixedValueScalarMetaType {
    private static final Set<String> values = Set.of("both", "parent");
    public OMTHandlersFromMetaType() {
        super("OMT From (MergeHandlers)");
    }

    @Override
    Set<String> getAcceptableValues() {
        return values;
    }
}
