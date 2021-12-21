package com.misset.opp.omt.meta.scalars.values;

import java.util.Set;

public class OMTDeclaredInterfaceTypeMetaType extends OMTFixedValueScalarMetaType {
    private static final Set<String> acceptableValues = Set.of("Activity", "Procedure", "Command", "Query");

    public OMTDeclaredInterfaceTypeMetaType() {
        super("DeclaredInterfaceType");
    }

    @Override
    Set<String> getAcceptableValues() {
        return acceptableValues;
    }
}
