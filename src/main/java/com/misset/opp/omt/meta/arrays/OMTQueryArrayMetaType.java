package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.scalars.ODTQueryMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTQueryArrayMetaType extends YamlArrayType {
    public OMTQueryArrayMetaType() {
        super(new ODTQueryMetaType());
    }

}
