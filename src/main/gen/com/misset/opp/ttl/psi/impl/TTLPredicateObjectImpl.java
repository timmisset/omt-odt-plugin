// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.ttl.psi.TTLObjectList;
import com.misset.opp.ttl.psi.TTLPredicateObject;
import com.misset.opp.ttl.psi.TTLVerb;
import com.misset.opp.ttl.psi.TTLVisitor;
import org.jetbrains.annotations.NotNull;

public class TTLPredicateObjectImpl extends ASTWrapperPsiElement implements TTLPredicateObject {

  public TTLPredicateObjectImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitPredicateObject(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TTLVisitor) accept((TTLVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public TTLObjectList getObjectList() {
    return findNotNullChildByClass(TTLObjectList.class);
  }

  @Override
  @NotNull
  public TTLVerb getVerb() {
    return findNotNullChildByClass(TTLVerb.class);
  }

}
