package com.misset.opp.omt.meta.model.modelitems.ontology;

import com.misset.opp.omt.meta.arrays.OMTOntologyClassesArrayMetaType;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemDelegateMetaType;
import com.misset.opp.omt.meta.scalars.OMTOntologyPrefixMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTOntologyMetaType extends OMTModelItemDelegateMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Set<String> requiredFields = Set.of("prefix", "classes");

    static {
        features.put("prefix", OMTOntologyPrefixMetaType::new);
        features.put("classes", OMTOntologyClassesArrayMetaType::new);
    }

    public OMTOntologyMetaType() {
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

    @Override
    public boolean isCallable() {
        return true;
    }

    @Override
    public String getType() {
        return "Ontology";
    }
}
