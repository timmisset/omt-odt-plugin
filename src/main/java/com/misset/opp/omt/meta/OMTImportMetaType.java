package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.arrays.OMTImportPathMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTImportMetaType extends OMTMetaMapType {
    protected OMTImportMetaType() {
        super("OMT Import");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTImportPathMetaType(name);
    }
}
