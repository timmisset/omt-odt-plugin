package com.misset.opp.omt.meta.model.scalars;

import com.misset.opp.omt.meta.markers.ODTInjectable;
import org.jetbrains.yaml.meta.model.YamlScalarType;

public class OMTQueryType extends YamlScalarType implements ODTInjectable {
    public OMTQueryType() {
        super("OMT Query");
    }
}
