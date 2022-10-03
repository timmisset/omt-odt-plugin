package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableValueAbstract extends ODTResolvableAbstract implements ODTResolvableValue {
    protected ODTResolvableValueAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        if (getQuery() != null) {
            return getQuery().resolve();
        }
        if (getCommandCall() != null) {
            return getCommandCall().resolve();
        }
        return Collections.emptySet();
    }

    private ODTResolvable getResolvable() {
        return Optional.ofNullable(getQuery())
                .map(ODTResolvable.class::cast)
                .orElse(getCommandCall());
    }

}
