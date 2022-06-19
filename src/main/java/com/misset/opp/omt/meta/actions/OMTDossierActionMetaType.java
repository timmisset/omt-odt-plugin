package com.misset.opp.omt.meta.actions;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.scalars.OMTInterpolatedStringMetaType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;

import java.util.HashMap;
import java.util.function.Supplier;

public class OMTDossierActionMetaType extends OMTMetaType implements OMTDocumented {
    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final OMTDossierActionMetaType INSTANCE = new OMTDossierActionMetaType();

    public static OMTDossierActionMetaType getInstance() {
        return INSTANCE;
    }

    static {
        features.put("title", OMTInterpolatedStringMetaType::getInstance);
        features.put("icon", YamlStringType::getInstance);
        features.put("params", OMTParamsArrayMetaType::getInstance);
    }

    private OMTDossierActionMetaType() {
        super("Dossier Action");
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public String getDocumentationClass() {
        return "DossierAction";
    }
}
