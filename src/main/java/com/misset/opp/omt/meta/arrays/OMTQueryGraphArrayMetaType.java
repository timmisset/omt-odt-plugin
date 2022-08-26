package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.scalars.queries.OMTGraphQueryType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTQueryGraphArrayMetaType extends YamlArrayType {
    private static final OMTQueryGraphArrayMetaType INSTANCE = new OMTQueryGraphArrayMetaType();

    public static OMTQueryGraphArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTQueryGraphArrayMetaType() {
        super(OMTGraphQueryType.getInstance());
    }
}
