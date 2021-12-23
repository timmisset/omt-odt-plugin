package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.actions.OMTDossierActionMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTDossierActionsArrayMetaType extends YamlArrayType {
    public OMTDossierActionsArrayMetaType() {
        super(new OMTDossierActionMetaType());
    }
}
