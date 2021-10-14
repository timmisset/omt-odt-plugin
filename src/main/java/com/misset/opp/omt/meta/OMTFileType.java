package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.model.OMTModelType;
import com.misset.opp.omt.meta.model.OMTPrefixesType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTCommandsType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTQueriesType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * The OMTFileType is the root for all OMT features
 * Any .omt file that is analysed can contain these features
 */
public class OMTFileType extends OMTMetaType {
    protected OMTFileType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    protected static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("import", OMTImport::new);
        features.put("model", OMTModelType::new);
        features.put("queries", OMTQueriesType::new);
        features.put("commands", OMTCommandsType::new);
        features.put("prefixes", OMTPrefixesType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
