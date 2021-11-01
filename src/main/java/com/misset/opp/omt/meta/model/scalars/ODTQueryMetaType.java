package com.misset.opp.omt.meta.model.scalars;

import com.misset.opp.omt.meta.ODTInjectable;
import org.jetbrains.yaml.meta.model.YamlScalarType;

@ODTInjectable
public class ODTQueryMetaType extends YamlScalarType {
    public ODTQueryMetaType() {
        super("ODT Query");
    }
}
