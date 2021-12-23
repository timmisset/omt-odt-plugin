package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.actions.OMTActionMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTActionsArrayMetaType extends YamlArrayType {
    public OMTActionsArrayMetaType() {
        super(new OMTActionMetaType(false));
    }

}
