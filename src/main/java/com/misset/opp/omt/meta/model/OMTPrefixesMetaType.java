package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.scalars.OMTIriMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTPrefixesMetaType extends OMTMetaMapType {

    public OMTPrefixesMetaType() {
        super("OMT Prefixes");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTIriMetaType();
    }
}
