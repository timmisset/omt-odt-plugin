// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.ttl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TTLTriplesImpl extends ASTWrapperPsiElement implements TTLTriples {

  public TTLTriplesImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitTriples(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TTLVisitor) accept((TTLVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TTLBlankNodePropertyList getBlankNodePropertyList() {
    return findChildByClass(TTLBlankNodePropertyList.class);
  }

  @Override
  @Nullable
  public TTLPredicateObjectList getPredicateObjectList() {
    return findChildByClass(TTLPredicateObjectList.class);
  }

  @Override
  @Nullable
  public TTLSubject getSubject() {
    return findChildByClass(TTLSubject.class);
  }

}
