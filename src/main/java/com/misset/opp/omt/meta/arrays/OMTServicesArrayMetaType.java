package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.OMTServiceMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTServicesArrayMetaType extends YamlArrayType {
    public OMTServicesArrayMetaType() {
        super(new OMTServiceMetaType());
    }
}
