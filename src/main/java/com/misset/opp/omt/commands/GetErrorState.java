package com.misset.opp.omt.commands;

import com.intellij.openapi.project.Project;
import com.misset.opp.model.OntologyModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class GetErrorState extends LocalCommand {
    public static final String CALLID = "@GET_ERROR_STATE";

    public GetErrorState(String source) {
        super(source);
    }

    @Override
    public String getName() {
        return "GET_ERROR_STATE";
    }

    @Override
    public String getDescription(Project project) {
        return String.format("Get the current error state for the %s", getSource());
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Set.of(OntologyModelConstants.getXsdStringInstance());
    }

    @Override
    public boolean isVoid() {
        return false;
    }
}
