// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi;

import com.misset.opp.odt.psi.resolvable.query.ODTResolvableQueryPath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ODTQueryPath extends ODTQuery, ODTResolvableQueryPath {

  @NotNull
  List<ODTQueryOperationStep> getQueryOperationStepList();

  @Nullable
  ODTRootIndicator getRootIndicator();

  @NotNull
  List<ODTStepSeperator> getStepSeperatorList();

}
