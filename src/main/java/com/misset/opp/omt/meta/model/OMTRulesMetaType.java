package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaMapType;
import org.jetbrains.yaml.meta.model.YamlMetaType;


public class OMTRulesMetaType extends OMTMetaMapType implements OMTDocumented {

    public OMTRulesMetaType() {
        super("OMTRules");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTRuleMetaType();
    }

    @Override
    public String getDocumentationClass() {
        return "Rule";
    }
}
