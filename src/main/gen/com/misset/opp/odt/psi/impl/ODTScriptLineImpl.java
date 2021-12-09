// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTScriptLineImpl extends ODTDocumentedScriptLine implements ODTScriptLine {

  public ODTScriptLineImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitScriptLine(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ODTCommandBlock getCommandBlock() {
    return findChildByClass(ODTCommandBlock.class);
  }

  @Override
  @Nullable
  public ODTDefineCommandStatement getDefineCommandStatement() {
    return findChildByClass(ODTDefineCommandStatement.class);
  }

  @Override
  @Nullable
  public ODTLogicalBlock getLogicalBlock() {
    return findChildByClass(ODTLogicalBlock.class);
  }

  @Override
  @Nullable
  public ODTStatement getStatement() {
    return findChildByClass(ODTStatement.class);
  }

}
