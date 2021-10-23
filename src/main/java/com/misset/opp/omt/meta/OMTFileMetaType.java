package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.model.OMTModelMetaType;
import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTCommandsMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTQueriesMetaType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * The OMTFileMetaType is the root for all OMT features
 * Any .omt file that is analysed can contain these features
 */
public class OMTFileMetaType extends OMTMetaType {
    protected OMTFileMetaType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    protected static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("import", OMTImportMetaType::new);
        features.put("model", OMTModelMetaType::new);
        features.put("queries", ODTQueriesMetaType::new);
        features.put("commands", ODTCommandsMetaType::new);
        features.put("prefixes", OMTPrefixesMetaType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
