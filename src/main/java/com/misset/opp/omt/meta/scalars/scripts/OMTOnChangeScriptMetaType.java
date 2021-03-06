package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.omt.meta.OMTLocalVariableProviderMetaType;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.List;

public class OMTOnChangeScriptMetaType extends OMTLocalVariableProviderMetaType {
    public OMTOnChangeScriptMetaType() {
        super("Script, triggers when the value changes");
    }

    @Override
    protected List<String> getLocalVariables(YAMLPsiElement element) {
        return List.of("$newValue", "$oldValue");
    }
}
