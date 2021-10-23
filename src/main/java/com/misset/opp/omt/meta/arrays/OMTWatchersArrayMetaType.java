package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.OMTQueryWatcherMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTWatchersArrayMetaType extends YamlArrayType {
    public OMTWatchersArrayMetaType() {
        super(new OMTQueryWatcherMetaType());
    }
}
