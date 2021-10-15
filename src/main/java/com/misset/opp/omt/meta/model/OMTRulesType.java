package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaMapType;
import org.jetbrains.yaml.meta.model.YamlMetaType;


public class OMTRulesType extends OMTMetaMapType {

    public OMTRulesType() {
        super("OMTRules");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTRuleType();
    }

}
