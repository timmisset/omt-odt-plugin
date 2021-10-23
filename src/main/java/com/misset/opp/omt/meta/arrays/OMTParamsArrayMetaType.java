package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTParamsArrayMetaType extends YamlArrayType {
    public OMTParamsArrayMetaType() {
        super(new OMTParamMetaType());
    }
}
