// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTChooseBlock;
import com.misset.opp.odt.psi.ODTEndPath;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.resolvable.querystep.choose.ODTResolvableChooseBlockStep;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ODTChooseBlockImpl extends ODTResolvableChooseBlockStep implements ODTChooseBlock {

  public ODTChooseBlockImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitChooseBlock(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ODTEndPath getEndPath() {
    return findChildByClass(ODTEndPath.class);
  }

  @Override
  @NotNull
  public List<ODTQueryStep> getQueryStepList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ODTQueryStep.class);
  }

}
