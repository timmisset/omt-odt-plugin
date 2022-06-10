package com.misset.opp.resolvable.local;

import com.intellij.openapi.project.Project;
import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class HasError extends LocalCommand {
    public static HasError INSTANCE = new HasError();

    protected HasError() {
    }

    @Override
    public String getName() {
        return "HAS_ERROR";
    }

    @Override
    public String getDescription(String context, Project project) {
        return String.format("Check if there is an error for the %s", context);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
    }

    @Override
    public boolean isVoid() {
        return false;
    }
}
