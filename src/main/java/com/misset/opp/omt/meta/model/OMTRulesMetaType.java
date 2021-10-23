package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.meta.OMTMetaMapType;
import org.jetbrains.yaml.meta.model.YamlMetaType;


public class OMTRulesMetaType extends OMTMetaMapType {

    public OMTRulesMetaType() {
        super("OMTRules");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTRuleMetaType();
    }

}
