// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCommandCallAbstract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTCommandCallImpl extends ODTCommandCallAbstract implements ODTCommandCall {

    public ODTCommandCallImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull ODTVisitor visitor) {
        visitor.visitCommandCall(this);
    }

    @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ODTCallName getCallName() {
    return findNotNullChildByClass(ODTCallName.class);
  }

  @Override
  @Nullable
  public ODTFlagSignature getFlagSignature() {
    return findChildByClass(ODTFlagSignature.class);
  }

  @Override
  @Nullable
  public ODTSignature getSignature() {
    return findChildByClass(ODTSignature.class);
  }

}
