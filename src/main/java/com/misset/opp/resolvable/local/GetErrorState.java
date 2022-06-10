package com.misset.opp.resolvable.local;

import com.intellij.openapi.project.Project;
import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GetErrorState extends LocalCommand {
    public static GetErrorState INSTANCE = new GetErrorState();

    protected GetErrorState() {
    }

    @Override
    public String getName() {
        return "GET_ERROR_STATE";
    }

    @Override
    public String getDescription(String context, Project project) {
        return String.format("Get the current error state for the %s", context);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE);
    }

    @Override
    public boolean isVoid() {
        return false;
    }
}
