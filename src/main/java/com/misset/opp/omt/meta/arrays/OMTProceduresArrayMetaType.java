package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.OMTProcedureMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTProceduresArrayMetaType extends YamlArrayType {
    public OMTProceduresArrayMetaType() {
        super(new OMTProcedureMetaType());
    }
}
