package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.scalars.queries.ODTGraphQueryType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTQueryGraphArrayMetaType extends YamlArrayType {
    public OMTQueryGraphArrayMetaType() {
        super(new ODTGraphQueryType());
    }

}
