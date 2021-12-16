package com.misset.opp.ttl.psi;

import com.intellij.psi.tree.IElementType;
import com.misset.opp.ttl.TTLLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class TTLTokenType extends IElementType {
    public TTLTokenType(@NotNull @NonNls String debugName) {
        super(debugName, TTLLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "TTLTokenType." + super.toString();
    }
}
