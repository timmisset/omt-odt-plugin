// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.ttl.psi.TTLBlankNodePropertyList;
import com.misset.opp.ttl.psi.TTLPredicateObjectList;
import com.misset.opp.ttl.psi.TTLVisitor;
import org.jetbrains.annotations.NotNull;

public class TTLBlankNodePropertyListImpl extends ASTWrapperPsiElement implements TTLBlankNodePropertyList {

  public TTLBlankNodePropertyListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitBlankNodePropertyList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TTLVisitor) accept((TTLVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TTLPredicateObjectList getPredicateObjectList() {
    return findNotNullChildByClass(TTLPredicateObjectList.class);
  }

}
