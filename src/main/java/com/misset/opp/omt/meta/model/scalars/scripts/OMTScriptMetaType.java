package com.misset.opp.omt.meta.model.scalars.scripts;

import com.misset.opp.omt.meta.ODTInjectable;
import org.jetbrains.yaml.meta.model.YamlScalarType;

@ODTInjectable
public class OMTScriptMetaType extends YamlScalarType {
    public OMTScriptMetaType() {
        super("ODT Script");
    }
    public OMTScriptMetaType(String typeName) {
        super(typeName);
    }
}