package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.scalars.OMTIriMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTPrefixesMetaType extends OMTMetaMapType {
    private static final OMTPrefixesMetaType INSTANCE = new OMTPrefixesMetaType();

    public static OMTPrefixesMetaType getInstance() {
        return INSTANCE;
    }

    private OMTPrefixesMetaType() {
        super("OMT Prefixes");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return OMTIriMetaType.getInstance();
    }
}
