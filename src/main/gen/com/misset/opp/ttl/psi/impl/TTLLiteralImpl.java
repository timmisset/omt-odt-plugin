// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.ttl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TTLLiteralImpl extends ASTWrapperPsiElement implements TTLLiteral {

  public TTLLiteralImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitLiteral(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TTLVisitor) accept((TTLVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TTLBooleanLiteral getBooleanLiteral() {
    return findChildByClass(TTLBooleanLiteral.class);
  }

  @Override
  @Nullable
  public TTLNumericLiteral getNumericLiteral() {
    return findChildByClass(TTLNumericLiteral.class);
  }

  @Override
  @Nullable
  public TTLRdfLiteral getRdfLiteral() {
    return findChildByClass(TTLRdfLiteral.class);
  }

}
