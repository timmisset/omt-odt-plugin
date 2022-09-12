// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.callable.ODTDefineCommandStatementAbstract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTDefineCommandStatementImpl extends ODTDefineCommandStatementAbstract implements ODTDefineCommandStatement {

  public ODTDefineCommandStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitDefineCommandStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ODTCommandBlock getCommandBlock() {
    return findNotNullChildByClass(ODTCommandBlock.class);
  }

  @Override
  @NotNull
  public ODTDefineName getDefineName() {
    return findNotNullChildByClass(ODTDefineName.class);
  }

  @Override
  @Nullable
  public ODTDefineParam getDefineParam() {
    return findChildByClass(ODTDefineParam.class);
  }

}
