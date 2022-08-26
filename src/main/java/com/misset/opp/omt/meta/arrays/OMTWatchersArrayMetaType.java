package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.OMTQueryWatcherMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTWatchersArrayMetaType extends YamlArrayType {

    private static final OMTWatchersArrayMetaType INSTANCE = new OMTWatchersArrayMetaType();

    public static OMTWatchersArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTWatchersArrayMetaType() {
        super(OMTQueryWatcherMetaType.getInstance());
    }
}
