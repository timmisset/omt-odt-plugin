package com.misset.opp.resolvable.local;

import com.misset.opp.resolvable.Variable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class LocalVariable implements Variable {
    private final String name;
    private final String description;
    private final Set<OntResource> type;

    public LocalVariable(String name,
                         String description,
                         Set<OntResource> type) {
        this.name = name;
        this.description = description;
        this.type = type;
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
}
