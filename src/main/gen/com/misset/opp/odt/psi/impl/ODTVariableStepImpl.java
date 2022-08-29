// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableStep;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.resolvable.querystep.ODTResolvableVariableStep;
import org.jetbrains.annotations.NotNull;

public class ODTVariableStepImpl extends ODTResolvableVariableStep implements ODTVariableStep {

  public ODTVariableStepImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitVariableStep(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ODTVariable getVariable() {
    return findNotNullChildByClass(ODTVariable.class);
  }

}
