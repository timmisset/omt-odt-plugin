package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.traverse.ODTResolvableQueryReverseStep;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableQueryStepBase extends ODTASTWrapperPsiElement
        implements ODTQueryStep, ODTResolvable {
    public ODTResolvableQueryStepBase(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * Method to calculate the ResourceSet for this QueryStep
     * Should be overridden by every implementation of ODTQueryStep
     */
    public abstract @NotNull Set<OntResource> resolve();

    /**
     * Returns the resolve QueryOperation container of this step
     * If steps are further encapsulated, this method should be overridden to return the QueryOperation
     */
    public ODTResolvableQueryOperationStep getResolvableParent() {
        return PsiTreeUtil.getParentOfType(this, ODTResolvableQueryOperationStep.class);
    }

    protected boolean isRootStep() {
        return getResolvableParent().isRootStep();
    }

    @Override
    public Set<OntResource> resolvePreviousStep() {
        return Optional.of(getResolvableParent())
                .map(ODTResolvableQueryOperationStep::resolvePreviousStep)
                .orElse(Collections.emptySet());
    }

    protected PsiElement getAnnotationRange() {
        return this;
    }

    @Override
    public String getDocumentation(Project project) {
        return null;
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        inspectResolved(holder, "FORWARD");
    }

    protected void inspectResolved(ProblemsHolder holder,
                                   String direction) {
        if (resolve().isEmpty() && !resolvePreviousStep().isEmpty()) {
            final String fullyQualifiedUri = getFullyQualifiedUri();
            if (fullyQualifiedUri != null) {
                holder.registerProblem(this,
                        "Could not traverse " + direction + " using predicate: " + fullyQualifiedUri);
            }
        }
    }

    protected String getFullyQualifiedUri() {
        return null;
    }
    protected boolean isPartOfReverseStep() {
        return PsiTreeUtil.getParentOfType(this, ODTQueryStep.class) instanceof ODTResolvableQueryReverseStep;
    }
}
