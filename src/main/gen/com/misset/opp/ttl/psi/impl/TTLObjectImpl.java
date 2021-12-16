// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.misset.opp.ttl.psi.*;
import com.misset.opp.ttl.psi.spo.TTLBaseObject;
import com.misset.opp.ttl.stubs.object.TTLObjectStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TTLObjectImpl extends TTLBaseObject implements TTLObject {

  public TTLObjectImpl(@NotNull TTLObjectStub stub, @NotNull IStubElementType<?, ?> type) {
    super(stub, type);
  }

  public TTLObjectImpl(@NotNull ASTNode node) {
    super(node);
  }

  public TTLObjectImpl(TTLObjectStub stub, IElementType type, ASTNode node) {
    super(stub, type, node);
  }

  public void accept(@NotNull TTLVisitor visitor) {
    visitor.visitObject(this);
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
  public TTLBlankNodePropertyList getBlankNodePropertyList() {
    return findChildByClass(TTLBlankNodePropertyList.class);
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

  @Override
  @Nullable
  public TTLLiteral getLiteral() {
    return findChildByClass(TTLLiteral.class);
  }

}
