package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTQueryArray;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ODTResolvableQueryArray extends ODTResolvableQuery implements ODTQueryArray {
    public ODTResolvableQueryArray(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> resolve() {
        return getQueryList()
                .stream()
                .map(ODTResolvable::resolve)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
