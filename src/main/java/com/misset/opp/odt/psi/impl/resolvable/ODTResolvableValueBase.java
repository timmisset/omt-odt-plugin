package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.resolvable.Resolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableValueBase extends ODTBaseResolvable implements ODTResolvableValue {
    protected ODTResolvableValueBase(@NotNull ASTNode node) {
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


    public ODTResolvable getResolvable() {
        return Optional.ofNullable(getQuery())
                .map(ODTResolvable.class::cast)
                .orElse(getCommandCall());
    }

    @Override
    public boolean isMultiple() {
        return Optional.ofNullable(getResolvable()).map(Resolvable::isMultiple).orElse(false);
    }


}
