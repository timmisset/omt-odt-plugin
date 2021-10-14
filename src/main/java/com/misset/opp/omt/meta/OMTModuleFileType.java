package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.arrays.OMTActionsArrayType;
import com.misset.opp.omt.meta.arrays.OMTProceduresArrayType;
import com.misset.opp.omt.meta.arrays.OMTServicesArrayType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionType;
import com.misset.opp.omt.meta.model.scalars.scripts.OMTScriptType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * The OMTFileType is the OMT Root and only contains specifically labelled features
 */
public class OMTModuleFileType extends OMTFileType {
    protected OMTModuleFileType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        // include all features from the root
        features.putAll(OMTFileType.features);

        features.put("moduleName", YamlStringType::new);
        features.put("graphs", OMTGraphSelectionType::new);
        features.put("onSessionStart", OMTScriptType::new);
        features.put("menu", OMTScriptType::new);
        features.put("actions", OMTActionsArrayType::new);
        features.put("services", OMTServicesArrayType::new);
        features.put("procedures", OMTProceduresArrayType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
