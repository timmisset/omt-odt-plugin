package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.variables.OMTParamType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTParamsArrayType extends YamlArrayType {
    public OMTParamsArrayType() {
        super(new OMTParamType());
    }
}
