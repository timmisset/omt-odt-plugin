package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.model.variables.OMTBindingItemType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTBindingType extends OMTMetaMapType {
    public OMTBindingType() {
        super("OMTBinding");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTBindingItemType();
    }
}
