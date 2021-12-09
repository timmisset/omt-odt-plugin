// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.ODTReturnStatement;
import com.misset.opp.odt.psi.ODTVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTReturnStatementImpl extends ODTStatementImpl implements ODTReturnStatement {

  public ODTReturnStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitReturnStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ODTResolvableValue getResolvableValue() {
    return findChildByClass(ODTResolvableValue.class);
  }

}
