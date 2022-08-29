// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTIdentifierStep;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.resolvable.querystep.ODTResolvableIdentifierStep;
import org.jetbrains.annotations.NotNull;

public class ODTIdentifierStepImpl extends ODTResolvableIdentifierStep implements ODTIdentifierStep {

  public ODTIdentifierStepImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitIdentifierStep(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

}
