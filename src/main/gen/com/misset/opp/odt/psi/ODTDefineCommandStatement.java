// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ODTDefineCommandStatement extends PsiElement {

    @NotNull
    ODTCommandBlock getCommandBlock();

    @NotNull
    ODTDefineName getDefineName();

    @Nullable
    ODTDefineParam getDefineParam();

}
