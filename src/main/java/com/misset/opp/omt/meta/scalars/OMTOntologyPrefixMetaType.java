package com.misset.opp.omt.meta.scalars;

import org.jetbrains.yaml.meta.model.YamlStringType;

public class OMTOntologyPrefixMetaType extends YamlStringType {
    // open issue https://github.com/timmisset/omt-odt-plugin/issues/128
    private static final OMTOntologyPrefixMetaType INSTANCE = new OMTOntologyPrefixMetaType();

    public static OMTOntologyPrefixMetaType getInstance() {
        return INSTANCE;
    }

    private OMTOntologyPrefixMetaType() {
        super();
    }
}
