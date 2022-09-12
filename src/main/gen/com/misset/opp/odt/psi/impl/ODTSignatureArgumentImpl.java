// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTCommandBlock;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTResolvableSignatureArgumentAbstract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTSignatureArgumentImpl extends ODTResolvableSignatureArgumentAbstract implements ODTSignatureArgument {

  public ODTSignatureArgumentImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitSignatureArgument(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ODTCommandBlock getCommandBlock() {
    return findChildByClass(ODTCommandBlock.class);
  }

  @Override
  @Nullable
  public ODTResolvableValue getResolvableValue() {
    return findChildByClass(ODTResolvableValue.class);
  }

}
