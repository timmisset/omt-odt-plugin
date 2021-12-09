// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.ODTVariableValue;
import com.misset.opp.odt.psi.ODTVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ODTVariableAssignmentImpl extends ODTStatementImpl implements ODTVariableAssignment {

  public ODTVariableAssignmentImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitVariableAssignment(this);
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
  public ODTVariableValue getVariableValue() {
    return findNotNullChildByClass(ODTVariableValue.class);
  }

}
