package com.misset.opp.omt.meta.actions;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTEntityBarActionMetaType extends OMTMetaType implements OMTDocumented {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("icon", YamlStringType::new);
    }

    public OMTEntityBarActionMetaType() {
        super("EntityBar Action");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public String getDocumentationClass() {
        return "EntitybarAction";
    }
}
