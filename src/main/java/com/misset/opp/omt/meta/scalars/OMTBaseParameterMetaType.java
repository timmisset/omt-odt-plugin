package com.misset.opp.omt.meta.scalars;

import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTBaseParameterMetaType extends OMTVariableNameMetaType implements OMTNamedVariableMetaType {
    private static final OMTBaseParameterMetaType INSTANCE = new OMTBaseParameterMetaType();

    public static OMTBaseParameterMetaType getInstance() {
        return INSTANCE;
    }

    private OMTBaseParameterMetaType() {
        super();
    }

    @Override
    public boolean isReadonly(YAMLValue value) {
        // although technically read-only, we don't want read-only formatting for a base parameter
        return false;
    }

}
