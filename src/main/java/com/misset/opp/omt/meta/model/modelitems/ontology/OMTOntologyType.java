package com.misset.opp.omt.meta.model.modelitems.ontology;

import com.misset.opp.omt.meta.arrays.OMTOntologyClassesArrayType;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemDelegate;
import com.misset.opp.omt.meta.model.scalars.OMTInterpolatedString;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTOntologyType extends OMTModelItemDelegate {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Set<String> requiredFields = Set.of("prefix", "classes");

    static {
        features.put("prefix", OMTInterpolatedString::new);
        features.put("classes", OMTOntologyClassesArrayType::new);
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

    @Override
    public boolean isCallable() {
        return true;
    }
}
