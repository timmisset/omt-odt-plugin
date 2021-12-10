// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTStepSeperator;
import com.misset.opp.odt.psi.ODTVisitor;
import org.jetbrains.annotations.NotNull;

public class ODTStepSeperatorImpl extends ODTASTWrapperPsiElement implements ODTStepSeperator {

  public ODTStepSeperatorImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitStepSeperator(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

}
