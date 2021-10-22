package com.misset.opp.omt.meta.model.scalars.scripts;

import com.misset.opp.omt.meta.ODTInjectable;
import org.jetbrains.yaml.meta.model.YamlScalarType;

public class OMTScriptType extends YamlScalarType implements ODTInjectable {
    public OMTScriptType() {
        super("ODT Script");
    }
    public OMTScriptType(String typeName) {
        super(typeName);
    }
}
