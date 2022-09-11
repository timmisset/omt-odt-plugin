package com.misset.opp.omt.commands;

import com.intellij.openapi.project.Project;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class HasError extends LocalCommand {
    public static final String CALLID = "@HAS_ERROR";

    public HasError(String source) {
        super(source);
    }

    @Override
    public String getName() {
        return "HAS_ERROR";
    }

    @Override
    public String getDescription(Project project) {
        return String.format("Check if there is an error for the %s", getSource());
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Set.of(OppModelConstants.getXsdBooleanInstance());
    }

    @Override
    public boolean isVoid() {
        return false;
    }
}
