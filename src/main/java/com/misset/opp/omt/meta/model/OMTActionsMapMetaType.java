package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.actions.OMTActionMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTActionsMapMetaType extends OMTMetaMapType implements OMTDocumented {

    private static final OMTActionsMapMetaType INSTANCE = new OMTActionsMapMetaType();

    public static OMTActionsMapMetaType getInstance() {
        return INSTANCE;
    }

    private OMTActionsMapMetaType() {
        super("Actions (map)");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return OMTActionMetaType.getInstance();
    }

    @Override
    public String getDocumentationClass() {
        return "Action";
    }
}
