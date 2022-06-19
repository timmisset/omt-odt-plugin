package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.omt.meta.OMTLocalVariableProviderMetaType;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.List;

public class OMTOnSelectScriptMetaType extends OMTLocalVariableProviderMetaType {
    private static final OMTOnSelectScriptMetaType INSTANCE = new OMTOnSelectScriptMetaType();

    public static OMTOnSelectScriptMetaType getInstance() {
        return INSTANCE;
    }

    private OMTOnSelectScriptMetaType() {
        super();
    }

    @Override
    protected List<String> getLocalVariables(YAMLPsiElement element) {
        return List.of("$value");
    }
}
