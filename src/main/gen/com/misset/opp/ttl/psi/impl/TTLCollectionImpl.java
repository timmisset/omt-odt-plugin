// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.ttl.psi.TTLCollection;
import com.misset.opp.ttl.psi.TTLObject;
import com.misset.opp.ttl.psi.TTLVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TTLCollectionImpl extends ASTWrapperPsiElement implements TTLCollection {

  public TTLCollectionImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitCollection(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TTLVisitor) accept((TTLVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<TTLObject> getObjectList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, TTLObject.class);
  }

}
