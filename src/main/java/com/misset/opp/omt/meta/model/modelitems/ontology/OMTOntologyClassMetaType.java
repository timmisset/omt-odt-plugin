package com.misset.opp.omt.meta.model.modelitems.ontology;

import com.misset.opp.omt.meta.OMTMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTOntologyClassMetaType extends OMTMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Set<String> requiredFields = Set.of("id", "properties");

    static {
        features.put("id", YamlStringType::new);
        features.put("properties", OMTOntologyPropertyMetaType::new);
    }
    public OMTOntologyClassMetaType() {
        super("OMT Ontology Class");
    }

    @Override
    protected Set<String> getRequiredFields() {
        return requiredFields;
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
