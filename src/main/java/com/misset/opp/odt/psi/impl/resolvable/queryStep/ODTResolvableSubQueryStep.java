package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTSubQuery;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTResolvableSubQueryStep extends ODTResolvableQueryStep implements ODTSubQuery {
    public ODTResolvableSubQueryStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> resolve() {
        return getQuery().resolve();
    }
}
