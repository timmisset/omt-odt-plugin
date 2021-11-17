package com.misset.opp.omt.meta.model.scalars.scripts;

import com.misset.opp.omt.meta.OMTLocalVariableProviderMetaType;

import java.util.List;

public class ODTOnChangeScriptMetaType extends OMTLocalVariableProviderMetaType {
    public ODTOnChangeScriptMetaType() {
        super("ODT Script (When the value changes)");
    }

    @Override
    protected List<String> getLocalVariables() {
        return List.of("$newValue", "$oldValue");
    }
}
