package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.meta.OMTMetaType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public abstract class OMTModelItemDelegate extends OMTMetaType {
    protected OMTModelItemDelegate(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    public abstract boolean isCallable();
}
