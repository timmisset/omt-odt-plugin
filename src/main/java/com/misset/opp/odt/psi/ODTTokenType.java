package com.misset.opp.odt.psi;

import com.intellij.psi.tree.IElementType;
import com.misset.opp.odt.ODTLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ODTTokenType extends IElementType {
    public ODTTokenType(@NotNull @NonNls String debugName) {
        super(debugName, ODTLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "ODTTokenType." + super.toString();
    }
}
