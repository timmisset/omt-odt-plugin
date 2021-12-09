// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTCommandCall;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvableValueBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTResolvableValueImpl extends ODTResolvableValueBase implements ODTResolvableValue {

  public ODTResolvableValueImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitResolvableValue(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ODTCommandCall getCommandCall() {
    return findChildByClass(ODTCommandCall.class);
  }

  @Override
  @Nullable
  public ODTQuery getQuery() {
    return findChildByClass(ODTQuery.class);
  }

}
