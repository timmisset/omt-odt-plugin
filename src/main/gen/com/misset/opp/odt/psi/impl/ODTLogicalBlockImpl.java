// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ODTLogicalBlockImpl extends ODTScriptLineImpl implements ODTLogicalBlock {

  public ODTLogicalBlockImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitLogicalBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ODTCommandBlock> getCommandBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ODTCommandBlock.class);
  }

  @Override
  @Nullable
  public ODTElseBlock getElseBlock() {
    return findChildByClass(ODTElseBlock.class);
  }

  @Override
  @NotNull
  public List<ODTIfBlock> getIfBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ODTIfBlock.class);
  }

}
