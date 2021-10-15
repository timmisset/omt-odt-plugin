package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.OMTQueryWatcherType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTWatchersArrayType extends YamlArrayType {
    public OMTWatchersArrayType() {
        super(new OMTQueryWatcherType());
    }
}
