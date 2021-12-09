// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ODTLogicalBlock extends ODTScriptLine {

  @NotNull
  List<ODTCommandBlock> getCommandBlockList();

  @Nullable
  ODTElseBlock getElseBlock();

  @NotNull
  List<ODTIfBlock> getIfBlockList();

}
