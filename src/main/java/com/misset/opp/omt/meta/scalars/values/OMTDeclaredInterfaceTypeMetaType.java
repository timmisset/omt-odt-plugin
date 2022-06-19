package com.misset.opp.omt.meta.scalars.values;

import java.util.Set;

public class OMTDeclaredInterfaceTypeMetaType extends OMTFixedValueScalarMetaType {
    private static final Set<String> acceptableValues = Set.of("Activity", "Procedure", "Command", "Query");

    private static final OMTDeclaredInterfaceTypeMetaType INSTANCE = new OMTDeclaredInterfaceTypeMetaType();

    public static OMTDeclaredInterfaceTypeMetaType getInstance() {
        return INSTANCE;
    }

    private OMTDeclaredInterfaceTypeMetaType() {
        super("DeclaredInterfaceType");
    }

    @Override
    Set<String> getAcceptableValues() {
        return acceptableValues;
    }
}
