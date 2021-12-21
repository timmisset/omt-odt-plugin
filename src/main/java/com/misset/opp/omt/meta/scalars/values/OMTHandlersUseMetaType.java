package com.misset.opp.omt.meta.scalars.values;

import java.util.Set;

public class OMTHandlersUseMetaType extends OMTFixedValueScalarMetaType {
    private static final Set<String> values = Set.of("current", "parent");

    public OMTHandlersUseMetaType() {
        super("OMT From (MergeHandlers)");
    }

    @Override
    Set<String> getAcceptableValues() {
        return values;
    }
}
