// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTQueryOperationStep;
import com.misset.opp.odt.psi.ODTQueryPath;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQueryPath;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ODTQueryPathImpl extends ODTResolvableQueryPath implements ODTQueryPath {

  public ODTQueryPathImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitQueryPath(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ODTQueryOperationStep> getQueryOperationStepList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ODTQueryOperationStep.class);
  }

}
