package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.actions.OMTGlobalActionMetaType;
import com.misset.opp.omt.meta.arrays.OMTGraphShapeHandlerArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTImportedMemberRefArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTServicesArrayMetaType;
import com.misset.opp.omt.meta.model.OMTActionsMapMetaType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * The OMTFileMetaType is the OMT Root and only contains specifically labelled features
 */
public class OMTModuleFileType extends OMTFileMetaType {
    private static final OMTModuleFileType INSTANCE = new OMTModuleFileType();

    public static OMTModuleFileType getInstance() {
        return INSTANCE;
    }

    private OMTModuleFileType() {
        super();
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        // include all features from the root
        features.putAll(OMTFileMetaType.features);
        features.put("moduleName", YamlStringType::getInstance);
        features.put("graphs", OMTGraphSelectionMetaType::getInstance);
        features.put("onSessionStart", OMTScriptMetaType::getInstance);
        features.put("menu", OMTActionsMapMetaType::getInstance);
        features.put("actions", OMTGlobalActionMetaType::getInstance);
        features.put("services", OMTServicesArrayMetaType::getInstance);
        features.put("procedures", OMTImportedMemberRefArrayMetaType::getInstance);
        features.put("export", OMTImportedMemberRefArrayMetaType::getInstance);
        features.put("handlers", OMTGraphShapeHandlerArrayMetaType::getInstance);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
