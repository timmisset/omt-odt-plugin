package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTModelType extends OMTMetaMapType {

    public OMTModelType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTModelItemType(name);
    }

}
