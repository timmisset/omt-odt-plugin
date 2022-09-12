// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi;

import com.misset.opp.odt.psi.resolvable.querystep.choose.ODTResolvableChooseBlockStep;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ODTChooseBlock extends ODTQueryStep, ODTResolvableChooseBlockStep {

    @Nullable
    ODTEndPath getEndPath();

    @NotNull
    List<ODTQueryStep> getQueryStepList();

}
