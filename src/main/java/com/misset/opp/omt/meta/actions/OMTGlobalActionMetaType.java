package com.misset.opp.omt.meta.actions;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTActionsArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTDossierActionsArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTEntityBarActionsArrayMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTGlobalActionMetaType extends OMTMetaType implements OMTDocumented {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final OMTGlobalActionMetaType INSTANCE = new OMTGlobalActionMetaType();

    public static OMTGlobalActionMetaType getInstance() {
        return INSTANCE;
    }

    static {
        features.put("notifications", OMTActionsArrayMetaType::getInstance);
        features.put("bottomNavigation", OMTActionsArrayMetaType::getInstance);
        features.put("dashboard", OMTActionsArrayMetaType::getInstance);
        features.put("dossier", OMTDossierActionsArrayMetaType::getInstance);
        features.put("fixed", OMTActionsArrayMetaType::getInstance);
        features.put("entitybar", OMTEntityBarActionsArrayMetaType::getInstance);
    }

    private OMTGlobalActionMetaType() {
        super("Global Action");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public String getDocumentationClass() {
        return "GlobalAction";
    }
}
