package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableQueryStep extends ASTWrapperPsiElement implements ODTQueryStep, ODTResolvable {
    public ODTResolvableQueryStep(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * Method to calculate the ResourceSet for this QueryStep
     * Should be overridden by every implementation of ODTQueryStep
     */
    @Override
    public Set<OntResource> resolve() {
        return Collections.emptySet();
    }

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

    protected Set<OntResource> resolvePreviousStep() {
        return Optional.of(getResolvableParent())
                .map(ODTResolvableQueryOperationStep::resolvePreviousStep)
                .orElse(Collections.emptySet());
    }
}
