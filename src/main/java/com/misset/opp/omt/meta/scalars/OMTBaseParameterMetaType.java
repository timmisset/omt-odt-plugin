package com.misset.opp.omt.meta.scalars;

import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import org.jetbrains.yaml.psi.YAMLValue;

public class OMTBaseParameterMetaType extends OMTVariableNameMetaType implements OMTNamedVariableMetaType {

    @Override
    public boolean isReadonly(YAMLValue value) {
        // although technically read-only, we don't want read-only formatting for a base parameter
        return false;
    }

}
