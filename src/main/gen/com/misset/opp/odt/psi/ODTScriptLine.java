// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi;

import com.intellij.psi.PsiJavaDocumentedElement;
import org.jetbrains.annotations.Nullable;

public interface ODTScriptLine extends PsiJavaDocumentedElement {

  @Nullable
  ODTCommandBlock getCommandBlock();

  @Nullable
  ODTDefineCommandStatement getDefineCommandStatement();

  @Nullable
  ODTLogicalBlock getLogicalBlock();

  @Nullable
  ODTStatement getStatement();

}
