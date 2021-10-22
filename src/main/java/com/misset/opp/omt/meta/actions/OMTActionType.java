package com.misset.opp.omt.meta.actions;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayType;
import com.misset.opp.omt.meta.model.scalars.ODTQueryType;
import com.misset.opp.omt.meta.model.scalars.OMTInterpolatedString;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptType;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class OMTActionType extends OMTMetaType implements OMTVariableProvider {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("id", YamlStringType::new);
        features.put("title", OMTInterpolatedString::new);
        features.put("description", OMTInterpolatedString::new);
        features.put("promoteSubMenuItemToMainMenu", () -> new YamlBooleanType("promoteSubMenuItemToMainMenu"));
        features.put("icon", YamlStringType::new);
        features.put("params", OMTParamsArrayType::new);
        features.put("precondition", ODTQueryType::new);
        features.put("disabled", () -> new YamlBooleanType("disabled"));
        features.put("busyDisabled", () -> new YamlBooleanType("disabled"));
        features.put("dynamicActionQuery", ODTQueryType::new);
        features.put("onSelect", OMTScriptType::new);
    }
    public OMTActionType() {
        super("Action");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return null;
    }

    @Override
    public @NotNull HashMap<String, List<PsiElement>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, List<PsiElement>> variableMap = new HashMap<>();
        OMTVariableProviderUtil.addSequenceToMap(mapping, "params", variableMap);

        return variableMap;
    }
}
