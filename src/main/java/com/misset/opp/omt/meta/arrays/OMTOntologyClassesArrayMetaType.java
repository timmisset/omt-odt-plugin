package com.misset.opp.omt.meta.arrays;

import com.misset.opp.omt.meta.model.modelitems.ontology.OMTOntologyClassMetaType;
import org.jetbrains.yaml.meta.model.YamlArrayType;

public class OMTOntologyClassesArrayMetaType extends YamlArrayType {

    private static final OMTOntologyClassesArrayMetaType INSTANCE = new OMTOntologyClassesArrayMetaType();

    public static OMTOntologyClassesArrayMetaType getInstance() {
        return INSTANCE;
    }

    private OMTOntologyClassesArrayMetaType() {
        super(OMTOntologyClassMetaType.getInstance());
    }
}
