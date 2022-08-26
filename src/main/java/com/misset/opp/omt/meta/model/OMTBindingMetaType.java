package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.model.variables.OMTBindingParameterMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTBindingMetaType extends OMTMetaMapType implements OMTDocumented {
    private static final OMTBindingMetaType INSTANCE = new OMTBindingMetaType();

    public static OMTBindingMetaType getInstance() {
        return INSTANCE;
    }

    private OMTBindingMetaType() {
        super("OMTBinding");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return OMTBindingParameterMetaType.getInstance();
    }

    @Override
    public String getDocumentationClass() {
        return "Binding";
    }
}
