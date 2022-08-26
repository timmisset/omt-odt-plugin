package com.misset.opp.omt.meta.actions;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.scalars.scripts.OMTActionScriptMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTNotificationActionMetaType extends OMTActionMetaType implements OMTDocumented {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Set<String> REQUIRED = Set.of(TITLE, DESCRIPTION);

    static {
        features.put("onMarkAsRead", OMTActionScriptMetaType::getInstance);
        features.put("onDelete", OMTActionScriptMetaType::getInstance);
        features.putAll(OMTActionMetaType.features);
    }

    public OMTNotificationActionMetaType() {
        super();
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public String getDocumentationClass() {
        return "NotificationAction";
    }

    @Override
    protected Set<String> getRequiredFields() {
        return REQUIRED;
    }
}
