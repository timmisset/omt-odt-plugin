package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.OMTImportMember;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTImportPathType extends YamlArrayType {
    private final String path;
    public OMTImportPathType(String path) {
        super(new OMTImportMember());
        this.path = path;
    }
}
