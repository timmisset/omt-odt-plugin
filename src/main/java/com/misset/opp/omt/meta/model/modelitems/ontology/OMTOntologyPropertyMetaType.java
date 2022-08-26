package com.misset.opp.omt.meta.model.modelitems.ontology;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaMapType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTOntologyPropertyMetaType extends OMTMetaMapType implements OMTDocumented {
    private static final OMTOntologyPropertyMetaType INSTANCE = new OMTOntologyPropertyMetaType();

    public static OMTOntologyPropertyMetaType getInstance() {
        return INSTANCE;
    }

    private OMTOntologyPropertyMetaType() {
        super("OMT Ontology Properties");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return OMTOntologyPropertyItemMetaType.getInstance();
    }

    @Override
    public String getDocumentationClass() {
        return "OntologyProperty";
    }
}
