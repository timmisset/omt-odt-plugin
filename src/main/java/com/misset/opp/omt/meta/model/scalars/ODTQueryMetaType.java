package com.misset.opp.omt.meta.model.scalars;

import com.misset.opp.omt.meta.ODTInjectable;
import org.jetbrains.yaml.meta.model.YamlScalarType;

public class ODTQueryMetaType extends YamlScalarType implements ODTInjectable {
    public ODTQueryMetaType() {
        super("ODT Query");
    }
}
