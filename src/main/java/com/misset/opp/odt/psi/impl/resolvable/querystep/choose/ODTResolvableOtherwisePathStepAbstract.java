package com.misset.opp.odt.psi.impl.resolvable.querystep.choose;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTOtherwisePath;
import com.misset.opp.odt.psi.impl.resolvable.querystep.ODTResolvableQueryStepAbstract;
import com.misset.opp.resolvable.Resolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableOtherwisePathStepAbstract extends ODTResolvableQueryStepAbstract implements ODTOtherwisePath {
    protected ODTResolvableOtherwisePathStepAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Optional.ofNullable(getQuery())
                .map(Resolvable::resolve)
                .orElse(Collections.emptySet());
    }

    @Override
    public PsiElement getAnnotationRange() {
        return getFirstChild();
    }
}
