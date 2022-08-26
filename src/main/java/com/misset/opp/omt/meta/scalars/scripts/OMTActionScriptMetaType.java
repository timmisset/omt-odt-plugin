package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.omt.meta.OMTLocalVariableProviderMetaType;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.List;

public class OMTActionScriptMetaType extends OMTLocalVariableProviderMetaType {
    private static final OMTActionScriptMetaType INSTANCE = new OMTOnSelectScriptMetaType();

    public static OMTActionScriptMetaType getInstance() {
        return INSTANCE;
    }

    private OMTActionScriptMetaType() {
        super();
public class OMTActionScriptMetaType extends OMTLocalVariableProviderMetaType {
    public OMTActionScriptMetaType() {
        super("onSelect");
    }

    @Override
    protected List<String> getLocalVariables(YAMLPsiElement element) {
        return List.of("$value");
    }
}
