package com.misset.opp.omt.meta.model.modelitems.ontology;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaMapType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public class OMTOntologyPropertyMetaType extends OMTMetaMapType implements OMTDocumented {

    protected OMTOntologyPropertyMetaType() {
        super("OMT Ontology Properties");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTOntologyPropertyItemMetaType();
    }

    @Override
    public String getDocumentationClass() {
        return "OntologyProperty";
    }
}
