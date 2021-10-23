package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionMetaType;
import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTServiceMetaType extends OMTMetaType {
    private static final Set<String> requiredFeatures = Set.of("params", "prefixes");
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("params", OMTParamsArrayMetaType::new);
        features.put("graphs", OMTGraphSelectionMetaType::new);
        features.put("prefixes", OMTPrefixesMetaType::new);
        features.put("onRequest", OMTScriptMetaType::new);
    }
    public OMTServiceMetaType() {
        super("OMT Service");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    protected Set<String> getRequiredFields() {
        return requiredFeatures;
    }
}
