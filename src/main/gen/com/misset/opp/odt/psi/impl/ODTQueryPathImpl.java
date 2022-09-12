// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQueryPathAbstract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ODTQueryPathImpl extends ODTResolvableQueryPathAbstract implements ODTQueryPath {

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

  @Override
  @Nullable
  public ODTRootIndicator getRootIndicator() {
    return findChildByClass(ODTRootIndicator.class);
  }

  @Override
  @NotNull
  public List<ODTStepSeperator> getStepSeperatorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ODTStepSeperator.class);
  }

}
