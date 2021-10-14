package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.model.scalars.OMTIriType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTPrefixesType extends OMTMetaMapType {

    public OMTPrefixesType() {
        super("OMT Prefixes");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTIriType();
    }
}
