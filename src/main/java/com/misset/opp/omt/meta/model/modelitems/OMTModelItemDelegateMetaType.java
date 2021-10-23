package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.meta.OMTMetaType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public abstract class OMTModelItemDelegateMetaType extends OMTMetaType {
    protected OMTModelItemDelegateMetaType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    public abstract boolean isCallable();
}
