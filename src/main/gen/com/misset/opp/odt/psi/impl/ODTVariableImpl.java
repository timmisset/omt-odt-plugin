// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.variable.ODTBaseVariable;
import org.jetbrains.annotations.NotNull;

public class ODTVariableImpl extends ODTBaseVariable implements ODTVariable {

  public ODTVariableImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitVariable(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

}
