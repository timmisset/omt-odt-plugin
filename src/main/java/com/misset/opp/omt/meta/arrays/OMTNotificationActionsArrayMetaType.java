package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.actions.OMTNotificationActionMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTNotificationActionsArrayMetaType extends YamlArrayType {
    private static final OMTNotificationActionsArrayMetaType INSTANCE = new OMTNotificationActionsArrayMetaType();

    private OMTNotificationActionsArrayMetaType() {
        super(new OMTNotificationActionMetaType());
    }

    public static OMTNotificationActionsArrayMetaType getInstance() {
        return INSTANCE;
    }
}
