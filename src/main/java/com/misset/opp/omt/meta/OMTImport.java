package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.arrays.OMTImportPathType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTImport extends OMTMetaMapType {
    protected OMTImport() {
        super("OMT Import");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTImportPathType(name);
    }
}
