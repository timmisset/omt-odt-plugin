package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTInterpolatedString;
import com.misset.opp.omt.meta.model.scalars.OMTTypeIdentifierType;
import org.jetbrains.yaml.meta.model.YamlArrayType;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlIntegerType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTOntologyType extends OMTMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Set<String> requiredFields = Set.of("prefix", "classes");

    static {
        features.put("prefix", OMTInterpolatedString::new);
        features.put("classes", () -> new YamlArrayType(new OMTOntologyClassType()));
    }

    public OMTOntologyType() {
        super("OMT Ontology");
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

class OMTOntologyClassType extends OMTMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Set<String> requiredFields = Set.of("id", "properties");

    static {
        features.put("id", YamlStringType::new);
        features.put("properties", OMTOntologyPropertyType::new);
    }
    public OMTOntologyClassType() {
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

class OMTOntologyPropertyType extends OMTMetaMapType {

    protected OMTOntologyPropertyType() {
        super("OMT Ontology Properties");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTOntologyPropertyItemType();
    }
}

class OMTOntologyPropertyItemType extends OMTMetaType {
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
