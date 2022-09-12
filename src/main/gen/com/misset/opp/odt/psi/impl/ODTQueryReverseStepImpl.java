// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.resolvable.querystep.traverse.ODTResolvableQueryReverseStepAbstract;
import org.jetbrains.annotations.NotNull;

public class ODTQueryReverseStepImpl extends ODTResolvableQueryReverseStepAbstract implements ODTQueryReverseStep {

    public ODTQueryReverseStepImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull ODTVisitor visitor) {
        visitor.visitQueryReverseStep(this);
    }

    @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ODTQueryStep getQueryStep() {
    return findNotNullChildByClass(ODTQueryStep.class);
  }

}
