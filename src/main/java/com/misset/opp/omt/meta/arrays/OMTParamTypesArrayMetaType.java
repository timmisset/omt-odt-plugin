package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.scalars.OMTParamTypeType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTParamTypesArrayMetaType extends YamlArrayType {
    public OMTParamTypesArrayMetaType() {
        super(new OMTParamTypeType());
    }
}
