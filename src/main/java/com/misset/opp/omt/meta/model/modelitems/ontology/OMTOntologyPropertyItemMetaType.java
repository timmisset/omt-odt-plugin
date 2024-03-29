package com.misset.opp.omt.meta.model.modelitems.ontology;

import com.misset.opp.omt.meta.OMTMetaInjectable;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import com.misset.opp.omt.meta.scalars.OMTTypeIdentifierMetaType;
import org.jetbrains.yaml.meta.model.YamlBooleanType;
import org.jetbrains.yaml.meta.model.YamlIntegerType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

@SimpleInjectable
public class OMTOntologyPropertyItemMetaType extends OMTMetaType implements OMTMetaInjectable {
    private static final OMTOntologyPropertyItemMetaType INSTANCE = new OMTOntologyPropertyItemMetaType();

    public static OMTOntologyPropertyItemMetaType getInstance() {
        return INSTANCE;
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Set<String> requiredFields = Set.of("type");

    static {
        features.put("type", OMTTypeIdentifierMetaType::getInstance);
        features.put("required", YamlBooleanType::getSharedInstance);
        features.put("multiple", YamlBooleanType::getSharedInstance);
        features.put("maxCardinality", () -> new YamlIntegerType(false));
    }

    private OMTOntologyPropertyItemMetaType() {
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
