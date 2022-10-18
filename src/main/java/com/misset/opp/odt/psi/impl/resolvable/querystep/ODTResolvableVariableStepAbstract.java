package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTVariableStep;
import com.misset.opp.resolvable.Context;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public abstract class ODTResolvableVariableStepAbstract extends ODTResolvableQueryStepAbstract implements ODTVariableStep {
    protected ODTResolvableVariableStepAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve(@Nullable Context context) {
        if (context == null) {
            return Collections.emptySet();
        }
        final Set<OntResource> paramType = context.getCall().getParamType(getVariable().getName());
        if (paramType.isEmpty()) {
            return resolve();
        }
        return paramType;
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getVariable().resolve();
    }
}
