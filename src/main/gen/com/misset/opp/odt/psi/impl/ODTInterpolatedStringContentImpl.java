// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTInterpolatedStringContent;
import com.misset.opp.odt.psi.ODTInterpolation;
import com.misset.opp.odt.psi.ODTVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTInterpolatedStringContentImpl extends ODTASTWrapperPsiElement implements ODTInterpolatedStringContent {

  public ODTInterpolatedStringContentImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitInterpolatedStringContent(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ODTInterpolation getInterpolation() {
    return findChildByClass(ODTInterpolation.class);
  }

}
