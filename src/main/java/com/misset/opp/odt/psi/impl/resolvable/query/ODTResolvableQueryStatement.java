package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTQueryStatement;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvableAbstract;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import com.misset.opp.resolvable.Context;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public abstract class ODTResolvableQueryStatement extends ODTResolvableAbstract implements ODTResolvable, ODTQueryStatement {
    protected ODTResolvableQueryStatement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getQuery().resolve();
    }

    @Override
    public @NotNull Set<OntResource> resolve(@Nullable Context context) {
        return getQuery().resolve(context);
    }

    @Override
    public @NotNull List<Literal> resolveLiteral() {
        return getQuery().resolveLiteral();
    }
}
