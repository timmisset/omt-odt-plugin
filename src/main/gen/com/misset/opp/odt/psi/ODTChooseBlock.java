// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi;

import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ODTChooseBlock extends ODTQueryStep, ODTResolvable {

  @Nullable
  ODTEndPath getEndPath();

  @NotNull
  List<ODTQueryStep> getQueryStepList();

}
