package com.misset.opp.omt.meta.scalars;

import org.jetbrains.yaml.meta.model.YamlStringType;

public class OMTOntologyPrefixMetaType extends YamlStringType {
    // todo: validate that the prefix is declared in the OMT file
    private static final OMTOntologyPrefixMetaType INSTANCE = new OMTOntologyPrefixMetaType();

    public static OMTOntologyPrefixMetaType getInstance() {
        return INSTANCE;
    }

    private OMTOntologyPrefixMetaType() {
        super();
    }
}
