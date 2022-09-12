// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.callable.ODTDefineQueryStatementAbstract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTDefineQueryStatementImplAbstract extends ODTDefineQueryStatementAbstract implements ODTDefineQueryStatement {

    public ODTDefineQueryStatementImplAbstract(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull ODTVisitor visitor) {
        visitor.visitDefineQueryStatement(this);
    }

    @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
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

  @Override
  @NotNull
  public ODTQuery getQuery() {
    return findNotNullChildByClass(ODTQuery.class);
  }

}
