package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.resolvable.CallableType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class OMTModelItemDelegateMetaType extends OMTMetaType implements OMTDocumented {
    private static final List<String> additionalDocumentationHeaders = List.of("Local Commands");

    protected OMTModelItemDelegateMetaType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    public abstract boolean isCallable();

    public abstract CallableType getType();

    @Override
    public List<String> getAdditionalDescriptionHeaders() {
        return additionalDocumentationHeaders;
    }
}
