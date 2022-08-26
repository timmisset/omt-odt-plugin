package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.variables.OMTVariableMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTVariablesArrayMetaType extends YamlArrayType {

    private static final OMTVariablesArrayMetaType INSTANCE = new OMTVariablesArrayMetaType();

    public static OMTVariablesArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTVariablesArrayMetaType() {
        super(OMTVariableMetaType.getInstance());
    }
}
