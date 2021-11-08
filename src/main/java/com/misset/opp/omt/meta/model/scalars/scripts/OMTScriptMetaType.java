package com.misset.opp.omt.meta.model.scalars.scripts;

import com.misset.opp.omt.meta.ODTInjectable;
import org.jetbrains.yaml.meta.model.YamlScalarType;

public class OMTScriptMetaType extends YamlScalarType implements ODTInjectable {
    boolean isExportable = false;

    public OMTScriptMetaType() {
        super("ODT Script");
    }
    public OMTScriptMetaType(String typeName) {
        super(typeName);
    }
    public OMTScriptMetaType(String typeName, boolean isExportable) {
        super(typeName);
        this.isExportable = isExportable;
    }

    public boolean isExportable() {
        return isExportable;
    }
}
