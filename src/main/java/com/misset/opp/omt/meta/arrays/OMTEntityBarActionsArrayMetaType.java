package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.actions.OMTEntityBarActionMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTEntityBarActionsArrayMetaType extends YamlArrayType {

    private static final OMTEntityBarActionsArrayMetaType INSTANCE = new OMTEntityBarActionsArrayMetaType();

    public static OMTEntityBarActionsArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTEntityBarActionsArrayMetaType() {
        super(OMTEntityBarActionMetaType.getInstance());
    }
}
