package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTQueryOperationStep;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.resolvable.querystep.ODTResolvableQueryStep;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableQueryStepAbstract extends ASTWrapperPsiElement implements ODTResolvableQueryStep, ODTQueryStep {
    protected ODTResolvableQueryStepAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public ODTQueryOperationStep getResolvableParent() {
        return PsiTreeUtil.getParentOfType(this, ODTQueryOperationStep.class);
    }

    @Override
    public boolean isRootStep() {
        return getResolvableParent().isRootStep();
    }

    @Override
    public Set<OntResource> resolvePreviousStep() {
        return Optional.of(getResolvableParent())
                .map(ODTQueryOperationStep::resolvePreviousStep)
                .orElse(Collections.emptySet());
    }

    @Override
    public PsiElement getAnnotationRange() {
        return this;
    }

    @Override
    public String getDocumentation(Project project) {
        return null;
    }

    protected boolean isPartOfReverseStep() {
        return PsiTreeUtil.getParentOfType(this, ODTQueryStep.class) instanceof ODTQueryReverseStep;
    }

    @Override
    public void inspect(ProblemsHolder holder) {

    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Collections.emptySet();
    }

    @Override
    public @NotNull List<Literal> resolveLiteral() {
        return Collections.emptyList();
    }
}
