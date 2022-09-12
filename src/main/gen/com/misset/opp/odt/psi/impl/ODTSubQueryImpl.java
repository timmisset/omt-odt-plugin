// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTSubQuery;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.resolvable.querystep.ODTResolvableSubQueryStepAbstract;
import org.jetbrains.annotations.NotNull;

public class ODTSubQueryImpl extends ODTResolvableSubQueryStepAbstract implements ODTSubQuery {

  public ODTSubQueryImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitSubQuery(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ODTQuery getQuery() {
    return findNotNullChildByClass(ODTQuery.class);
  }

}
