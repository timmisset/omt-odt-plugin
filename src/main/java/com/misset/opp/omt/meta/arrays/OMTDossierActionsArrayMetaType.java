package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.actions.OMTDossierActionMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTDossierActionsArrayMetaType extends YamlArrayType {

    private static final OMTDossierActionsArrayMetaType INSTANCE = new OMTDossierActionsArrayMetaType();

    public static OMTDossierActionsArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTDossierActionsArrayMetaType() {
        super(OMTDossierActionMetaType.getInstance());
    }
}
