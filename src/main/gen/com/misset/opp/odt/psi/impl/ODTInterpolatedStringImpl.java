// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTInterpolatedString;
import com.misset.opp.odt.psi.ODTInterpolatedStringContent;
import com.misset.opp.odt.psi.ODTVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ODTInterpolatedStringImpl extends ODTConstantValueImpl implements ODTInterpolatedString {

  public ODTInterpolatedStringImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitInterpolatedString(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ODTInterpolatedStringContent> getInterpolatedStringContentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ODTInterpolatedStringContent.class);
  }

}
