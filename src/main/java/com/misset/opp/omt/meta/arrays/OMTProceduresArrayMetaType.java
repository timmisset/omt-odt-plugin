package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.OMTProcedureMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTProceduresArrayMetaType extends YamlArrayType {
    private static final OMTProceduresArrayMetaType INSTANCE = new OMTProceduresArrayMetaType();

    public static OMTProceduresArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTProceduresArrayMetaType() {
        super(OMTProcedureMetaType.getInstance());
    }
}
