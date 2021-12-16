// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.ttl.psi.TTLSparqlPrefix;
import com.misset.opp.ttl.psi.TTLVisitor;
import org.jetbrains.annotations.NotNull;

public class TTLSparqlPrefixImpl extends TTLDirectiveImpl implements TTLSparqlPrefix {

  public TTLSparqlPrefixImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitSparqlPrefix(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TTLVisitor) accept((TTLVisitor) visitor);
    else super.accept(visitor);
  }

}
