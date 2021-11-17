package com.misset.opp.callable.local;

import com.misset.opp.callable.Variable;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.Set;

public class LocalVariable implements Variable {
    private String name;
    private String description;
    private Set<OntResource> type;

    public LocalVariable(String name) {
        this(name, name, Collections.emptySet());
    }

    public LocalVariable(String name,
                         String description) {
        this(name, description, Collections.emptySet());
    }

    public LocalVariable(String name,
                         Set<OntResource> type) {
        this(name, name, type);
    }

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
    public Set<OntResource> getType() {
        return type;
    }
}
