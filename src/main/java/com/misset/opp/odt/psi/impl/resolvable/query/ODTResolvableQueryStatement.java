package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTQueryStatement;
import com.misset.opp.odt.psi.impl.resolvable.ODTBaseResolvable;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.resolvable.Context;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTResolvableQueryStatement extends ODTBaseResolvable implements ODTResolvable, ODTQueryStatement {
    protected ODTResolvableQueryStatement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getQuery().resolve();
    }

    @Override
    public @NotNull Set<OntResource> resolve(Context context) {
        return getQuery().resolve(context);
    }
}
