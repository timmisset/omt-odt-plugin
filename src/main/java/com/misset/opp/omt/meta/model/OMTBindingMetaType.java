package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.model.variables.OMTBindingItemMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTBindingMetaType extends OMTMetaMapType {
    public OMTBindingMetaType() {
        super("OMTBinding");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTBindingItemMetaType();
    }
}
