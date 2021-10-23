package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.OMTImportMemberMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTImportPathMetaType extends YamlArrayType {
    private final String path;
    public OMTImportPathMetaType(String path) {
        super(new OMTImportMemberMetaType());
        this.path = path;
    }
}
