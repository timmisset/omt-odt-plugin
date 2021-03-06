// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.ODTWhenPath;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.choose.ODTResolvableWhenPathStep;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ODTWhenPathImpl extends ODTResolvableWhenPathStep implements ODTWhenPath {

  public ODTWhenPathImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitWhenPath(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ODTQuery> getQueryList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ODTQuery.class);
  }

}
