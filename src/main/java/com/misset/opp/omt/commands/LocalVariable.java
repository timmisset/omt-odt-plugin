package com.misset.opp.omt.commands;

import com.misset.opp.resolvable.Variable;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class LocalVariable implements Variable {
    private final String name;
    private final String description;
    private final Set<OntResource> type;
    private final String source;

    public LocalVariable(String name,
                         String description,
                         Set<OntResource> type,
                         String source) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.source = source;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return type;
    }

    @Override
    public boolean isReadonly() {
        return true;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public Scope getScope() {
        return Scope.LOCAL;
    }

    @Override
    public @NotNull List<Literal> resolveLiteral() {
        return Collections.emptyList();
    }
}
