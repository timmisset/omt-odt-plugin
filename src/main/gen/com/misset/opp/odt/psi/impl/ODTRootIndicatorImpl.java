// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTRootIndicator;
import com.misset.opp.odt.psi.ODTVisitor;
import org.jetbrains.annotations.NotNull;

public class ODTRootIndicatorImpl extends ODTASTWrapperPsiElement implements ODTRootIndicator {

  public ODTRootIndicatorImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitRootIndicator(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

}
