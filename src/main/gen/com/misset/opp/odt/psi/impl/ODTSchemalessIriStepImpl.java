// This is a generated file. Not intended for manual editing.
package com.misset.opp.odt.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTSchemalessIriStep;
import com.misset.opp.odt.psi.ODTVisitor;
import com.misset.opp.odt.psi.impl.resolvable.querystep.traverse.ODTResolvableSchemalessIriStepAbstract;
import org.jetbrains.annotations.NotNull;

public class ODTSchemalessIriStepImpl extends ODTResolvableSchemalessIriStepAbstract implements ODTSchemalessIriStep {

    public ODTSchemalessIriStepImpl(@NotNull ASTNode node) {
        super(node);
    }

    public void accept(@NotNull ODTVisitor visitor) {
        visitor.visitSchemalessIriStep(this);
    }

    @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ODTVisitor) accept((ODTVisitor) visitor);
    else super.accept(visitor);
  }

}
