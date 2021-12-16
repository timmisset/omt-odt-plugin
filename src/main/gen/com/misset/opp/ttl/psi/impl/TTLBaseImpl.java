// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.ttl.psi.TTLBase;
import com.misset.opp.ttl.psi.TTLVisitor;
import org.jetbrains.annotations.NotNull;

public class TTLBaseImpl extends TTLDirectiveImpl implements TTLBase {

  public TTLBaseImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitBase(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TTLVisitor) accept((TTLVisitor) visitor);
    else super.accept(visitor);
  }

}
