package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.actions.OMTActionMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTActionsMetaType  extends OMTMetaMapType {
    public OMTActionsMetaType() {
        super("Actions (map)");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTActionMetaType();
    }
}
