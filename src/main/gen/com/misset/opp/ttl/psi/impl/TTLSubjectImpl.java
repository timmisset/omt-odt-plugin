// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.ttl.psi.*;
import com.misset.opp.ttl.psi.spo.TTLBaseSubject;
import com.misset.opp.ttl.stubs.subject.TTLSubjectStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TTLSubjectImpl extends TTLBaseSubject implements TTLSubject {

  public TTLSubjectImpl(@NotNull TTLSubjectStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TTLSubjectImpl(@NotNull ASTNode node) {
    super(node);
  }

  public TTLSubjectImpl(TTLSubjectStub stub, IElementType type, ASTNode node) {
    super(stub, type, node);
  }

  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitSubject(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof TTLVisitor) accept((TTLVisitor) visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public TTLBlankNode getBlankNode() {
    return findChildByClass(TTLBlankNode.class);
  }

  @Override
  @Nullable
  public TTLCollection getCollection() {
    return findChildByClass(TTLCollection.class);
  }

  @Override
  @Nullable
  public TTLIri getIri() {
    return findChildByClass(TTLIri.class);
  }

}
