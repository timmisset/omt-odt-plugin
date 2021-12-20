// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.PsiNamedElement;
import com.misset.opp.odt.documentation.ODTDocumented;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQueryStep;
import com.misset.opp.odt.psi.impl.variable.ODTVariableWrapper;
import org.jetbrains.annotations.NotNull;

public class ODTVisitor extends PsiElementVisitor {

  public void visitAddToCollection(@NotNull ODTAddToCollection o) {
    visitCollectionStatement(o);
  }

  public void visitAssignmentStatement(@NotNull ODTAssignmentStatement o) {
    visitCollectionStatement(o);
  }

  public void visitBooleanStatement(@NotNull ODTBooleanStatement o) {
    visitQuery(o);
  }

  public void visitCallName(@NotNull ODTCallName o) {
    visitDocumented(o);
  }

  public void visitChooseBlock(@NotNull ODTChooseBlock o) {
    visitQueryStep(o);
    // visitResolvable(o);
  }

  public void visitCollectionStatement(@NotNull ODTCollectionStatement o) {
    visitStatement(o);
    // visitResolvableCollectionStatement(o);
  }

  public void visitCommandBlock(@NotNull ODTCommandBlock o) {
    visitPsiElement(o);
  }

  public void visitCommandCall(@NotNull ODTCommandCall o) {
    visitStatement(o);
    // visitCall(o);
  }

  public void visitConstantValue(@NotNull ODTConstantValue o) {
    visitQueryStep(o);
  }

  public void visitCurieElement(@NotNull ODTCurieElement o) {
    visitQueryStep(o);
  }

  public void visitDeclareVariable(@NotNull ODTDeclareVariable o) {
    visitStatement(o);
  }

  public void visitDefineCommandStatement(@NotNull ODTDefineCommandStatement o) {
    visitPsiElement(o);
  }

  public void visitDefineName(@NotNull ODTDefineName o) {
    visitPsiElement(o);
  }

  public void visitDefineParam(@NotNull ODTDefineParam o) {
    visitPsiElement(o);
  }

  public void visitDefinePrefix(@NotNull ODTDefinePrefix o) {
    visitStatement(o);
  }

  public void visitDefineQueryStatement(@NotNull ODTDefineQueryStatement o) {
    visitStatement(o);
  }

  public void visitElseBlock(@NotNull ODTElseBlock o) {
    visitPsiElement(o);
  }

  public void visitEndPath(@NotNull ODTEndPath o) {
    visitPsiElement(o);
  }

  public void visitEquationStatement(@NotNull ODTEquationStatement o) {
    visitQuery(o);
  }

  public void visitFlagSignature(@NotNull ODTFlagSignature o) {
    visitPsiElement(o);
  }

  public void visitIdentifierStep(@NotNull ODTIdentifierStep o) {
    visitQueryStep(o);
  }

  public void visitIfBlock(@NotNull ODTIfBlock o) {
    visitPsiElement(o);
  }

  public void visitInterpolatedString(@NotNull ODTInterpolatedString o) {
    visitConstantValue(o);
  }

  public void visitInterpolatedStringContent(@NotNull ODTInterpolatedStringContent o) {
    visitPsiElement(o);
  }

  public void visitInterpolation(@NotNull ODTInterpolation o) {
    visitPsiElement(o);
  }

  public void visitIriStep(@NotNull ODTIriStep o) {
    visitQueryStep(o);
  }

  public void visitLogicalBlock(@NotNull ODTLogicalBlock o) {
    visitScriptLine(o);
  }

  public void visitNamespacePrefix(@NotNull ODTNamespacePrefix o) {
    visitPsiNamedElement(o);
  }

  public void visitNegatedStep(@NotNull ODTNegatedStep o) {
    visitQueryStep(o);
  }

  public void visitOperatorCall(@NotNull ODTOperatorCall o) {
    visitQueryStep(o);
    // visitCall(o);
  }

  public void visitOtherwisePath(@NotNull ODTOtherwisePath o) {
    visitQueryStep(o);
    // visitResolvable(o);
  }

  public void visitQuery(@NotNull ODTQuery o) {
    visitResolvable(o);
  }

  public void visitQueryArray(@NotNull ODTQueryArray o) {
    visitQuery(o);
  }

  public void visitQueryFilter(@NotNull ODTQueryFilter o) {
    visitPsiElement(o);
  }

  public void visitQueryOperationStep(@NotNull ODTQueryOperationStep o) {
    visitPsiElement(o);
  }

  public void visitQueryPath(@NotNull ODTQueryPath o) {
    visitQuery(o);
  }

  public void visitQueryReverseStep(@NotNull ODTQueryReverseStep o) {
    visitQueryStep(o);
  }

  public void visitQueryStatement(@NotNull ODTQueryStatement o) {
    visitStatement(o);
  }

  public void visitQueryStep(@NotNull ODTQueryStep o) {
    visitResolvableQueryStep(o);
  }

  public void visitRangeSelection(@NotNull ODTRangeSelection o) {
    visitPsiElement(o);
  }

  public void visitRemoveFromCollection(@NotNull ODTRemoveFromCollection o) {
    visitCollectionStatement(o);
  }

  public void visitResolvableValue(@NotNull ODTResolvableValue o) {
    visitResolvable(o);
  }

  public void visitReturnStatement(@NotNull ODTReturnStatement o) {
    visitStatement(o);
  }

  public void visitRootIndicator(@NotNull ODTRootIndicator o) {
    visitPsiElement(o);
  }

  public void visitSchemalessIriStep(@NotNull ODTSchemalessIriStep o) {
    visitQueryStep(o);
  }

  public void visitScript(@NotNull ODTScript o) {
    visitPsiElement(o);
  }

  public void visitScriptLine(@NotNull ODTScriptLine o) {
    visitPsiJavaDocumentedElement(o);
  }

  public void visitSignature(@NotNull ODTSignature o) {
    visitPsiElement(o);
  }

  public void visitSignatureArgument(@NotNull ODTSignatureArgument o) {
    visitResolvable(o);
  }

  public void visitStatement(@NotNull ODTStatement o) {
    visitPsiElement(o);
  }

  public void visitStepDecorator(@NotNull ODTStepDecorator o) {
    visitPsiElement(o);
  }

  public void visitStepSeperator(@NotNull ODTStepSeperator o) {
    visitPsiElement(o);
  }

  public void visitSubQuery(@NotNull ODTSubQuery o) {
    visitQueryStep(o);
  }

  public void visitVariable(@NotNull ODTVariable o) {
    visitVariableWrapper(o);
  }

  public void visitVariableAssignment(@NotNull ODTVariableAssignment o) {
    visitStatement(o);
  }

  public void visitVariableStep(@NotNull ODTVariableStep o) {
    visitQueryStep(o);
  }

  public void visitVariableValue(@NotNull ODTVariableValue o) {
    visitPsiElement(o);
  }

  public void visitWhenPath(@NotNull ODTWhenPath o) {
    visitQueryStep(o);
    // visitResolvable(o);
  }

  public void visitPsiJavaDocumentedElement(@NotNull PsiJavaDocumentedElement o) {
    visitElement(o);
  }

  public void visitPsiNamedElement(@NotNull PsiNamedElement o) {
    visitElement(o);
  }

  public void visitDocumented(@NotNull ODTDocumented o) {
    visitPsiElement(o);
  }

  public void visitResolvable(@NotNull ODTResolvable o) {
    visitPsiElement(o);
  }

  public void visitResolvableQueryStep(@NotNull ODTResolvableQueryStep o) {
    visitPsiElement(o);
  }

  public void visitVariableWrapper(@NotNull ODTVariableWrapper o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull ODTPsiElement o) {
    visitElement(o);
  }

}
