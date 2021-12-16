// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.ttl.psi.TTLDirective;
import com.misset.opp.ttl.psi.TTLStatement;
import com.misset.opp.ttl.psi.TTLTriples;
import com.misset.opp.ttl.psi.TTLVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TTLStatementImpl extends ASTWrapperPsiElement implements TTLStatement {

  public TTLStatementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitStatement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TTLVisitor) accept((TTLVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TTLDirective getDirective() {
    return findChildByClass(TTLDirective.class);
  }

  @Override
  @Nullable
  public TTLTriples getTriples() {
    return findChildByClass(TTLTriples.class);
  }

}
