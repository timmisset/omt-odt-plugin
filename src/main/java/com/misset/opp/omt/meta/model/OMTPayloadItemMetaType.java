package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.model.scalars.ODTQueryMetaType;
import com.misset.opp.omt.meta.model.scalars.references.OMTPayloadQueryReferenceMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTOnChangeScriptMetaType;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTPayloadItemMetaType extends OMTMetaType implements ODTInjectable {
    private static final Set<String> requiredFeatures = Set.of("value");
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("value", ODTQueryMetaType::new);
        features.put("query", OMTPayloadQueryReferenceMetaType::new);
        features.put("list", () -> new YamlBooleanType("list"));
        features.put("onChange", ODTOnChangeScriptMetaType::new);
    }

    public OMTPayloadItemMetaType() {
        super("OMT PayloadItem");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    protected Set<String> getRequiredFields() {
        return requiredFeatures;
    }
}
