// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.ttl.psi.TTLIri;
import com.misset.opp.ttl.psi.TTLPredicate;
import com.misset.opp.ttl.psi.TTLVisitor;
import org.jetbrains.annotations.NotNull;

public class TTLPredicateImpl extends ASTWrapperPsiElement implements TTLPredicate {

  public TTLPredicateImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitPredicate(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TTLVisitor) accept((TTLVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TTLIri getIri() {
    return findNotNullChildByClass(TTLIri.class);
  }

}
