package com.misset.opp.odt.psi.impl.resolvable;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import com.misset.opp.resolvable.Resolvable;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableValueAbstract extends ODTResolvableAbstract implements ODTResolvableValue {
    protected ODTResolvableValueAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Optional.ofNullable(getResolvable())
                .map(Resolvable::resolve)
                .orElse(Collections.emptySet());
    }

    @Override
    @NotNull
    public List<Literal> resolveLiteral() {
        return Optional.ofNullable(getResolvable())
                .map(Resolvable::resolveLiteral)
                .orElse(Collections.emptyList());
    }

    private ODTResolvable getResolvable() {
        return Optional.ofNullable(getQuery())
                .map(ODTResolvable.class::cast)
                .orElse(getCommandCall());
    }

}
