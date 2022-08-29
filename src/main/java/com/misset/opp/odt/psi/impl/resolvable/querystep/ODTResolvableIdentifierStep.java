package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTIdentifierStep;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTResolvableIdentifierStep extends ODTResolvableQueryStepBase implements ODTIdentifierStep {
    protected ODTResolvableIdentifierStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return resolvePreviousStep();
    }
}
