package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.actions.OMTNotificationActionMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTNotificationActionsArrayMetaType extends YamlArrayType {
    public OMTNotificationActionsArrayMetaType() {
        super(new OMTNotificationActionMetaType(false));
    }
}
