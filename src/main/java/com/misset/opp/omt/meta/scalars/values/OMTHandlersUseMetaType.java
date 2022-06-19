package com.misset.opp.omt.meta.scalars.values;

import java.util.Set;

public class OMTHandlersUseMetaType extends OMTFixedValueScalarMetaType {

    private static final OMTHandlersUseMetaType INSTANCE = new OMTHandlersUseMetaType();

    public static OMTHandlersUseMetaType getInstance() {
        return INSTANCE;
    }

    private static final Set<String> values = Set.of("current", "parent");

    private OMTHandlersUseMetaType() {
        super("OMT From (MergeHandlers)");
    }

    @Override
    Set<String> getAcceptableValues() {
        return values;
    }
}
