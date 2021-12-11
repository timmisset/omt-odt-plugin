// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.ODTVisitor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ODTScriptImpl extends ODTASTWrapperPsiElement implements ODTScript {

  public ODTScriptImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ODTVisitor visitor) {
    visitor.visitScript(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ODTScriptLine> getScriptLineList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ODTScriptLine.class);
  }

}