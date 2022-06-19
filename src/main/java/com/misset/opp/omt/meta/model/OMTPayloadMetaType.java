package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaMapType;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.List;

public class OMTPayloadMetaType extends OMTMetaMapType implements OMTDocumented {
    private static final OMTPayloadMetaType INSTANCE = new OMTPayloadMetaType();

    public static OMTPayloadMetaType getInstance() {
        return INSTANCE;
    }

    private static final List<String> additionalDocumentationHeaders = List.of("Example Interaction with Payload from JavaScript");

    private OMTPayloadMetaType() {
        super("OMT Payload");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return OMTPayloadItemMetaType.getInstance();
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
