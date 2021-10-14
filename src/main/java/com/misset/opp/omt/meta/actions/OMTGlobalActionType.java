package com.misset.opp.omt.meta.actions;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTActionsArrayType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTGlobalActionType extends OMTMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("notifications", OMTActionsArrayType::new);
        features.put("bottomNavigation", OMTActionsArrayType::new);
        features.put("dashboard", OMTActionsArrayType::new);
        features.put("dossier", OMTActionsArrayType::new);
        features.put("fixed", OMTActionsArrayType::new);
        features.put("entitybar", OMTActionsArrayType::new);
    }
    public OMTGlobalActionType() {
        super("Action");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return null;
    }

}
