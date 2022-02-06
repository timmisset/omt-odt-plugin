package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaMapType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.List;

public class OMTPayloadMetaType extends OMTMetaMapType implements OMTDocumented {
    private static final List<String> additionalDocumentationHeaders = List.of("Example Interaction with Payload from JavaScript");

    public OMTPayloadMetaType() {
        super("OMT Payload");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTPayloadItemMetaType();
    }

    @Override
    public String getDocumentationClass() {
        return "PayloadProperty";
    }

    @Override
    public List<String> getAdditionalDescriptionHeaders() {
        return additionalDocumentationHeaders;
    }
}
