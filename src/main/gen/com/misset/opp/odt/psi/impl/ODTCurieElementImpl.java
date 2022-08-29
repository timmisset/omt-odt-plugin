// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTCurieElement;
import com.misset.opp.odt.psi.ODTNamespacePrefix;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.resolvable.querystep.traverse.ODTResolvableCurieElementStep;
import org.jetbrains.annotations.NotNull;

public class ODTCurieElementImpl extends ODTResolvableCurieElementStep implements ODTCurieElement {

  public ODTCurieElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitCurieElement(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public ODTNamespacePrefix getNamespacePrefix() {
    return findNotNullChildByClass(ODTNamespacePrefix.class);
  }

}
