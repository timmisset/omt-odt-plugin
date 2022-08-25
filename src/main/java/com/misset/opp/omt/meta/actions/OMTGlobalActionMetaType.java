package com.misset.opp.omt.meta.actions;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTActionsArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTDossierActionsArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTEntityBarActionsArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTNotificationActionsArrayMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class OMTGlobalActionMetaType extends OMTMetaType implements OMTDocumented {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("notifications", OMTNotificationActionsArrayMetaType::new);
        features.put("bottomNavigation", OMTActionsArrayMetaType::new);
        features.put("dashboard", OMTActionsArrayMetaType::new);
        features.put("dashboardActions", OMTActionsArrayMetaType::new);
        features.put("dossier", OMTDossierActionsArrayMetaType::new);
        features.put("fixed", OMTActionsArrayMetaType::new);
        features.put("bestandstatus", OMTActionsArrayMetaType::new);
        features.put("entitybar", OMTEntityBarActionsArrayMetaType::new);
    }

    public OMTGlobalActionMetaType() {
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

    @Override
    public List<String> getAdditionalHeaders() {
        return new ArrayList<>(features.keySet());
    }
}
