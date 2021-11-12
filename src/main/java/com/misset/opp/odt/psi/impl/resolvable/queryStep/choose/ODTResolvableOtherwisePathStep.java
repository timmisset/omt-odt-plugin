package com.misset.opp.odt.psi.impl.resolvable.queryStep.choose;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Resolvable;
import com.misset.opp.odt.psi.ODTOtherwisePath;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQueryStepBase;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableOtherwisePathStep extends ODTResolvableQueryStepBase implements ODTOtherwisePath {
    public ODTResolvableOtherwisePathStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Optional.ofNullable(getQuery())
                .map(Resolvable::resolve)
                .orElse(Collections.emptySet());
    }

    @Override
    protected PsiElement getAnnotationRange() {
        return getFirstChild();
    }
}
