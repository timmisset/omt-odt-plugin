package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaMapType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTPayloadType extends OMTMetaMapType {

    public OMTPayloadType() {
        super("OMT Payload");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTPayloadItemType();
    }
}
