package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import com.misset.opp.callable.Call;
import com.misset.opp.odt.psi.ODTVariableStep;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTResolvableVariableStep extends ODTResolvableQueryStepBase implements ODTVariableStep {
    public ODTResolvableVariableStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> resolve(Set<OntResource> resources,
                                    Call call) {
        final Set<OntResource> paramType = call.getParamType(getVariable().getName());
        if (paramType.isEmpty()) {
            return resolve();
        }
        return paramType;
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getVariable().getType();
    }
}
