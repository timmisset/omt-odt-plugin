package com.misset.opp.omt.meta.model.modelitems.ontology;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTOntologyClassMetaType extends OMTMetaType implements OMTDocumented {
    private static final OMTOntologyClassMetaType INSTANCE = new OMTOntologyClassMetaType();

    public static OMTOntologyClassMetaType getInstance() {
        return INSTANCE;
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Set<String> requiredFields = Set.of("id", "properties");

    static {
        features.put("id", YamlStringType::getInstance);
        features.put("properties", OMTOntologyPropertyMetaType::getInstance);
    }

    private OMTOntologyClassMetaType() {
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

    @Override
    public String getDocumentationClass() {
        return "OntologyClass";
    }
}
