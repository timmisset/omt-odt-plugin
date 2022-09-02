// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ODTQueryOperationStep extends PsiElement {

    @NotNull
    List<ODTQueryFilter> getQueryFilterList();

    @Nullable
    ODTQueryStep getQueryStep();

    @Nullable
    ODTStepDecorator getStepDecorator();

}
