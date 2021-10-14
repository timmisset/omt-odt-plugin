package com.misset.opp.omt.meta.model.modelitems.ontology;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTTypeIdentifierType;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlIntegerType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTOntologyPropertyItemType extends OMTMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Set<String> requiredFields = Set.of("type");

    static {
        features.put("type", OMTTypeIdentifierType::new);
        features.put("required", () -> new YamlBooleanType("required"));
        features.put("multiple", () -> new YamlBooleanType("multiple"));
        features.put("maxCardinality", () -> new YamlIntegerType(false));
    }

    public OMTOntologyPropertyItemType() {
        super("OMT Ontology Property");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    protected Set<String> getRequiredFields() {
        return requiredFields;
    }
}
