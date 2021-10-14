package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.scalars.OMTODTQueryType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTQueryArrayType extends YamlArrayType {
    public OMTQueryArrayType() {
        super(new OMTODTQueryType());
    }

}
