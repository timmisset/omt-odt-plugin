// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTDeclareVariable;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.variable.ODTBaseDeclaredVariable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ODTDeclareVariableImpl extends ODTBaseDeclaredVariable implements ODTDeclareVariable {

  public ODTDeclareVariableImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitDeclareVariable(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ODTVariable> getVariableList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ODTVariable.class);
  }

  @Override
  @NotNull
  public List<ODTVariableAssignment> getVariableAssignmentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ODTVariableAssignment.class);
  }

}
