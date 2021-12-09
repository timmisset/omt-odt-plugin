// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTSignature;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.ODTVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ODTSignatureImpl extends ODTASTWrapperPsiElement implements ODTSignature {

  public ODTSignatureImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitSignature(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ODTSignatureArgument> getSignatureArgumentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ODTSignatureArgument.class);
  }

}
