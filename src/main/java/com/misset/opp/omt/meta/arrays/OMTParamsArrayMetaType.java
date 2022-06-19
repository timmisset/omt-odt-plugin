package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTParamsArrayMetaType extends YamlArrayType {

    private static final OMTParamsArrayMetaType INSTANCE = new OMTParamsArrayMetaType();

    public static OMTParamsArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTParamsArrayMetaType() {
        super(OMTParamMetaType.getInstance());
    }
}
