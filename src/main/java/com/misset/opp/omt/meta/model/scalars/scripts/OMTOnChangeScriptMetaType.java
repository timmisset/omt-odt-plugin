package com.misset.opp.omt.meta.model.scalars.scripts;

import com.misset.opp.omt.meta.OMTLocalVariableProviderMetaType;

import java.util.List;

public class OMTOnChangeScriptMetaType extends OMTLocalVariableProviderMetaType {
    public OMTOnChangeScriptMetaType() {
        super("Script, triggers when the value changes");
    }

    @Override
    protected List<String> getLocalVariables() {
        return List.of("$newValue", "$oldValue");
    }
}
