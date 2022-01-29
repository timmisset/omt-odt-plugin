package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.module.OMTDeclareMetaType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * The OMTFileMetaType is the OMT Root and only contains specifically labelled features
 */
public class OMTInterfaceFileType extends OMTFileMetaType {
    protected OMTInterfaceFileType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("prefixes", OMTPrefixesMetaType::new);
        features.put("declare", OMTDeclareMetaType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
