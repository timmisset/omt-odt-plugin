package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.actions.OMTGlobalActionMetaType;
import com.misset.opp.omt.meta.arrays.OMTGraphShapeHandlerArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTImportedMemberRefArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTServicesArrayMetaType;
import com.misset.opp.omt.meta.model.OMTActionsMapMetaType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * The OMTFileMetaType is the OMT Root and only contains specifically labelled features
 */
public class OMTModuleFileType extends OMTFileMetaType {
    protected OMTModuleFileType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        // include all features from the root
        features.putAll(OMTFileMetaType.features);
        features.put("moduleName", YamlStringType::new);
        features.put("graphs", OMTGraphSelectionMetaType::new);
        features.put("onSessionStart", OMTScriptMetaType::new);
        features.put("menu", OMTActionsMapMetaType::new);
        features.put("actions", OMTGlobalActionMetaType::new);
        features.put("services", OMTServicesArrayMetaType::new);
        features.put("procedures", OMTImportedMemberRefArrayMetaType::new);
        features.put("export", OMTImportedMemberRefArrayMetaType::new);
        features.put("handlers", OMTGraphShapeHandlerArrayMetaType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
