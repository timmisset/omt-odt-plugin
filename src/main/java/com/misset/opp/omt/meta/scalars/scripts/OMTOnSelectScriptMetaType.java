package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.omt.meta.OMTLocalVariableProviderMetaType;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.List;

public class OMTOnSelectScriptMetaType extends OMTLocalVariableProviderMetaType {
    public OMTOnSelectScriptMetaType() {
        super("onSelect");
    }

    @Override
    protected List<String> getLocalVariables(YAMLPsiElement element) {
        return List.of("$value");
    }
}
