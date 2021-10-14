package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.variables.OMTVariableType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTVariablesArrayType extends YamlArrayType {
    public OMTVariablesArrayType() {
        super(new OMTVariableType());
    }
}
