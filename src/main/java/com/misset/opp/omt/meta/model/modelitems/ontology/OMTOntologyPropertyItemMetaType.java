package com.misset.opp.omt.meta.model.modelitems.ontology;

import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTTypeIdentifierMetaType;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlIntegerType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTOntologyPropertyItemMetaType extends OMTMetaType implements ODTInjectable {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Set<String> requiredFields = Set.of("type");

    static {
        features.put("type", OMTTypeIdentifierMetaType::new);
        features.put("required", YamlBooleanType::getSharedInstance);
        features.put("multiple", YamlBooleanType::getSharedInstance);
        features.put("maxCardinality", () -> new YamlIntegerType(false));
    }

    public OMTOntologyPropertyItemMetaType() {
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
