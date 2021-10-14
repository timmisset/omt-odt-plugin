package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionType;
import com.misset.opp.omt.meta.model.OMTPrefixesType;
import com.misset.opp.omt.meta.model.scalars.OMTODTQueryType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableName;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTStandaloneQueryType extends OMTMetaType {
    protected OMTStandaloneQueryType() {
        super("OMT Component");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("base", OMTVariableName::new);
        features.put("params", OMTParamsArrayType::new);
        features.put("graphs", OMTGraphSelectionType::new);
        features.put("prefixes", OMTPrefixesType::new);
        features.put("query", OMTODTQueryType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
