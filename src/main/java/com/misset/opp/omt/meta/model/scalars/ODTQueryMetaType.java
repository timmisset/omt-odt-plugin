package com.misset.opp.omt.meta.model.scalars;

import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.model.ODTSimpleInjectable;
import org.jetbrains.yaml.meta.model.YamlScalarType;

@ODTSimpleInjectable
public class ODTQueryMetaType extends YamlScalarType  implements ODTInjectable {
    public ODTQueryMetaType() {
        super("ODT Query");
    }
}
