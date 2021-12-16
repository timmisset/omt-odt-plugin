// This is a generated file. Not intended for manual editing.
package com.misset.opp.ttl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.ttl.psi.iri.TTLIriHolder;
import com.misset.opp.ttl.psi.prefix.TTLPrefixHolder;
import com.misset.opp.ttl.psi.prefix.TTLPrefixedNameHolder;
import com.misset.opp.ttl.psi.spo.TTLSPO;
import org.jetbrains.annotations.NotNull;

public class TTLVisitor extends PsiElementVisitor {

  public void visitBase(@NotNull TTLBase o) {
    visitDirective(o);
  }

  public void visitBlankNode(@NotNull TTLBlankNode o) {
    visitPsiElement(o);
  }

  public void visitBlankNodePropertyList(@NotNull TTLBlankNodePropertyList o) {
    visitPsiElement(o);
  }

  public void visitBooleanLiteral(@NotNull TTLBooleanLiteral o) {
    visitPsiElement(o);
  }

  public void visitCollection(@NotNull TTLCollection o) {
    visitPsiElement(o);
  }

  public void visitDeclaredBaseUri(@NotNull TTLDeclaredBaseUri o) {
    visitPsiElement(o);
  }

  public void visitDeclaredImportUri(@NotNull TTLDeclaredImportUri o) {
    visitPsiElement(o);
  }

  public void visitDirective(@NotNull TTLDirective o) {
    visitPsiElement(o);
  }

  public void visitIri(@NotNull TTLIri o) {
    visitIriHolder(o);
  }

  public void visitLiteral(@NotNull TTLLiteral o) {
    visitPsiElement(o);
  }

  public void visitNumericLiteral(@NotNull TTLNumericLiteral o) {
    visitPsiElement(o);
  }

  public void visitObject(@NotNull TTLObject o) {
    visitSPO(o);
  }

  public void visitObjectList(@NotNull TTLObjectList o) {
    visitPsiElement(o);
  }

  public void visitPredicate(@NotNull TTLPredicate o) {
    visitPsiElement(o);
  }

  public void visitPredicateObject(@NotNull TTLPredicateObject o) {
    visitPsiElement(o);
  }

  public void visitPredicateObjectList(@NotNull TTLPredicateObjectList o) {
    visitPsiElement(o);
  }

  public void visitPrefixId(@NotNull TTLPrefixId o) {
    visitPrefixHolder(o);
  }

  public void visitPrefixedName(@NotNull TTLPrefixedName o) {
    visitPrefixedNameHolder(o);
  }

  public void visitRdfLiteral(@NotNull TTLRdfLiteral o) {
    visitPsiElement(o);
  }

  public void visitSparqlBase(@NotNull TTLSparqlBase o) {
    visitDirective(o);
  }

  public void visitSparqlPrefix(@NotNull TTLSparqlPrefix o) {
    visitDirective(o);
  }

  public void visitStatement(@NotNull TTLStatement o) {
    visitPsiElement(o);
  }

  public void visitString(@NotNull TTLString o) {
    visitPsiElement(o);
  }

  public void visitSubject(@NotNull TTLSubject o) {
    visitSPO(o);
  }

  public void visitTriples(@NotNull TTLTriples o) {
    visitPsiElement(o);
  }

  public void visitVerb(@NotNull TTLVerb o) {
    visitPsiElement(o);
  }

  public void visitIriHolder(@NotNull TTLIriHolder o) {
    visitPsiElement(o);
  }

  public void visitPrefixHolder(@NotNull TTLPrefixHolder o) {
    visitPsiElement(o);
  }

  public void visitPrefixedNameHolder(@NotNull TTLPrefixedNameHolder o) {
    visitPsiElement(o);
  }

  public void visitSPO(@NotNull TTLSPO o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
