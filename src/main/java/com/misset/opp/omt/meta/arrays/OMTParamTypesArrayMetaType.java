package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.scalars.OMTParamTypeMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTParamTypesArrayMetaType extends YamlArrayType {
    private static final OMTParamTypesArrayMetaType INSTANCE = new OMTParamTypesArrayMetaType();

    public static OMTParamTypesArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTParamTypesArrayMetaType() {
        super(OMTParamTypeMetaType.getInstance());
    }
}
