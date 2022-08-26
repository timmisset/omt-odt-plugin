package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.omt.meta.OMTLocalVariableProviderMetaType;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.List;

public class OMTOnChangeScriptMetaType extends OMTLocalVariableProviderMetaType {

    private static final OMTOnChangeScriptMetaType INSTANCE = new OMTOnChangeScriptMetaType();

    public static final String ONCHANGE_VARIABLE = "OnChange variable";

    public static OMTOnChangeScriptMetaType getInstance() {
        return INSTANCE;
    }

    private OMTOnChangeScriptMetaType() {
        super();
    }

    @Override
    protected List<String> getLocalVariables(YAMLPsiElement element) {
        return List.of("$newValue", "$oldValue");
    }
}
