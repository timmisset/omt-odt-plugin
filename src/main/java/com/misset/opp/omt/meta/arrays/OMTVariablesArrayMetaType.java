package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.variables.OMTVariableMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTVariablesArrayMetaType extends YamlArrayType {
    public OMTVariablesArrayMetaType() {
        super(new OMTVariableMetaType());
    }
}
