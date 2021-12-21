package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.omt.meta.OMTInjectable;
import org.jetbrains.yaml.meta.model.YamlScalarType;

public class OMTScriptMetaType extends YamlScalarType implements OMTInjectable {
    boolean isExportable = false;

    public OMTScriptMetaType() {
        super("Script");
    }

    public OMTScriptMetaType(String typeName) {
        super(typeName);
    }

    public OMTScriptMetaType(String typeName,
                             boolean isExportable) {
        super(typeName);
        this.isExportable = isExportable;
    }

    public boolean isExportable() {
        return isExportable;
    }
}
