// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.ttl.psi.TTLPrefixId;
import com.misset.opp.ttl.psi.TTLVisitor;
import com.misset.opp.ttl.psi.prefix.TTLBasePrefixId;
import org.jetbrains.annotations.NotNull;

public class TTLPrefixIdImpl extends TTLBasePrefixId implements TTLPrefixId {

  public TTLPrefixIdImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitPrefixId(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TTLVisitor) accept((TTLVisitor) visitor);
    else super.accept(visitor);
  }

}
