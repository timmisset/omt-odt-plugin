package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.actions.OMTActionMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTActionsArrayMetaType extends YamlArrayType {
    private static final OMTActionsArrayMetaType INSTANCE = new OMTActionsArrayMetaType();

    public static OMTActionsArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTActionsArrayMetaType() {
        super(OMTActionMetaType.getInstance());
    }

}
