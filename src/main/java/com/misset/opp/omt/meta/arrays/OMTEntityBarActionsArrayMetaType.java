package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.actions.OMTEntityBarActionMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTEntityBarActionsArrayMetaType extends YamlArrayType {
    public OMTEntityBarActionsArrayMetaType() {
        super(new OMTEntityBarActionMetaType());
    }
}
