package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.model.OMTModelType;
import com.misset.opp.omt.meta.model.OMTPrefixesType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * The OMTFileType is the OMT Root and only contains specifically labelled features
 */
public class OMTFileType extends OMTMetaType {
    protected OMTFileType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("model", OMTModelType::new);
        features.put("prefixes", OMTPrefixesType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
