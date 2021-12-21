package com.misset.opp.omt.meta.module;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamTypesArrayMetaType;
import com.misset.opp.omt.meta.scalars.OMTParamTypeType;
import com.misset.opp.omt.meta.scalars.values.OMTDeclaredInterfaceTypeMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTDeclaredInterfaceMetaType extends OMTMetaType {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("type", OMTDeclaredInterfaceTypeMetaType::new);
        features.put("params", OMTParamTypesArrayMetaType::new);
        features.put("returns", OMTParamTypeType::new);
    }

    protected OMTDeclaredInterfaceMetaType() {
        super("DeclaredInterface");
    }

    @Override
    protected Set<String> getRequiredFields() {
        return Set.of("type");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
