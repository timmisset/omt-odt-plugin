// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTOtherwisePath;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.resolvable.querystep.choose.ODTResolvableOtherwisePathStep;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ODTOtherwisePathImpl extends ODTResolvableOtherwisePathStep implements ODTOtherwisePath {

  public ODTOtherwisePathImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitOtherwisePath(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ODTQuery getQuery() {
    return findChildByClass(ODTQuery.class);
  }

}
