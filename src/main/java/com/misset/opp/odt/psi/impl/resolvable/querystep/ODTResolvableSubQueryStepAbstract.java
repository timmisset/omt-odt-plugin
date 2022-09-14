package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTSubQuery;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTResolvableSubQueryStepAbstract extends ODTResolvableQueryStepAbstract implements ODTSubQuery {
    protected ODTResolvableSubQueryStepAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getQuery().resolve();
    }

}
