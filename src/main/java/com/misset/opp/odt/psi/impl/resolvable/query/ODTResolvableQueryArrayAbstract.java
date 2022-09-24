package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTQueryArray;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ODTResolvableQueryArrayAbstract extends ODTResolvableQueryAbstract implements ODTQueryArray {
    protected ODTResolvableQueryArrayAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getQueryList()
                .stream()
                .map(ODTResolvable::resolve)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<OntResource> filter(Set<OntResource> resources) {
        /*
            A query array is not a boolean statement, so it shouldn't be used in filters
         */
        return resources;
    }

    @Override
    public boolean requiresInput() {
        return getQueryList().stream().anyMatch(ODTQuery::requiresInput);
    }
}
