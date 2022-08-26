package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaMapType;
import org.jetbrains.yaml.meta.model.YamlMetaType;


public class OMTRulesMetaType extends OMTMetaMapType implements OMTDocumented {

    private static final OMTRulesMetaType INSTANCE = new OMTRulesMetaType();

    public static OMTRulesMetaType getInstance() {
        return INSTANCE;
    }

    private OMTRulesMetaType() {
        super("OMTRules");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return OMTRuleMetaType.getInstance();
    }

    @Override
    public String getDocumentationClass() {
        return "Rule";
    }
}
