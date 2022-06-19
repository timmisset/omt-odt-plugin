package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.omt.meta.OMTMetaInjectable;
import org.jetbrains.yaml.meta.model.YamlScalarType;

public class OMTScriptMetaType extends YamlScalarType implements OMTMetaInjectable {
    private static final OMTScriptMetaType INSTANCE = new OMTScriptMetaType();

    public static OMTScriptMetaType getInstance() {
        return INSTANCE;
    }

    protected OMTScriptMetaType() {
        super("Script");
    }

    public boolean isExportable() {
        return false;
    }
}
