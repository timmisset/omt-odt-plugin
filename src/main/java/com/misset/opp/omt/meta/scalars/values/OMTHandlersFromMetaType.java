package com.misset.opp.omt.meta.scalars.values;

import java.util.Set;

public class OMTHandlersFromMetaType extends OMTFixedValueScalarMetaType {
    private static final OMTHandlersFromMetaType INSTANCE = new OMTHandlersFromMetaType();

    public static OMTHandlersFromMetaType getInstance() {
        return INSTANCE;
    }

    private static final Set<String> values = Set.of("both", "parent");

    private OMTHandlersFromMetaType() {
        super("OMT From (MergeHandlers)");
    }

    @Override
    Set<String> getAcceptableValues() {
        return values;
    }
}
