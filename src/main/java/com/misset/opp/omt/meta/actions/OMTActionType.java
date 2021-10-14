package com.misset.opp.omt.meta.actions;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayType;
import com.misset.opp.omt.meta.model.scalars.OMTInterpolatedString;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTActionType extends OMTMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("id", YamlStringType::new);
        features.put("title", OMTInterpolatedString::new);
        features.put("description", OMTInterpolatedString::new);
        features.put("promoteSubMenuItemToMainMenu", () -> new YamlBooleanType("promoteSubMenuItemToMainMenu"));
        features.put("icon", YamlStringType::new);
        features.put("params", OMTParamsArrayType::new);

    }
    public OMTActionType() {
        super("Action");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return null;
    }

}
