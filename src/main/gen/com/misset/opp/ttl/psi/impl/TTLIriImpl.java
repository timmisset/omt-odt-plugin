// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.ttl.psi.TTLIri;
import com.misset.opp.ttl.psi.TTLPrefixedName;
import com.misset.opp.ttl.psi.TTLVisitor;
import com.misset.opp.ttl.psi.iri.TTLBaseIri;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TTLIriImpl extends TTLBaseIri implements TTLIri {

  public TTLIriImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitIri(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TTLVisitor) accept((TTLVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TTLPrefixedName getPrefixedName() {
    return findChildByClass(TTLPrefixedName.class);
  }

}
