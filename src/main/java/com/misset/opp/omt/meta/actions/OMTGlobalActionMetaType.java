package com.misset.opp.omt.meta.actions;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTActionsArrayMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTGlobalActionMetaType extends OMTMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("notifications", OMTActionsArrayMetaType::new);
        features.put("bottomNavigation", OMTActionsArrayMetaType::new);
        features.put("dashboard", OMTActionsArrayMetaType::new);
        features.put("dossier", OMTActionsArrayMetaType::new);
        features.put("fixed", OMTActionsArrayMetaType::new);
        features.put("entitybar", OMTActionsArrayMetaType::new);
    }
    public OMTGlobalActionMetaType() {
        super("Action");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return null;
    }

}
