package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.scalars.queries.OMTGraphQueryType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTQueryGraphArrayMetaType extends YamlArrayType {
    public OMTQueryGraphArrayMetaType() {
        super(new OMTGraphQueryType());
    }
}
