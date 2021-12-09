// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTAddToCollection;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.ODTVisitor;
import org.jetbrains.annotations.NotNull;

public class ODTAddToCollectionImpl extends ODTCollectionStatementImpl implements ODTAddToCollection {

  public ODTAddToCollectionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitAddToCollection(this);
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

  @Override
  @NotNull
  public ODTResolvableValue getResolvableValue() {
    return findNotNullChildByClass(ODTResolvableValue.class);
  }

}
