package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.OMTServiceMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTServicesArrayMetaType extends YamlArrayType {

    private static final OMTServicesArrayMetaType INSTANCE = new OMTServicesArrayMetaType();

    public static OMTServicesArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTServicesArrayMetaType() {
        super(OMTServiceMetaType.getInstance());
    }
}
