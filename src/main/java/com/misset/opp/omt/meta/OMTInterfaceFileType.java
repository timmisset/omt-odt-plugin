package com.misset.opp.omt.meta;

import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.module.OMTDeclareMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * The OMTFileMetaType is the OMT Root and only contains specifically labelled features
 */
public class OMTInterfaceFileType extends OMTFileMetaType {
    private static final OMTInterfaceFileType INSTANCE = new OMTInterfaceFileType();

    public static OMTInterfaceFileType getInstance() {
        return INSTANCE;
    }

    private OMTInterfaceFileType() {
        super();
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("prefixes", OMTPrefixesMetaType::getInstance);
        features.put("declare", OMTDeclareMetaType::getInstance);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }
}
