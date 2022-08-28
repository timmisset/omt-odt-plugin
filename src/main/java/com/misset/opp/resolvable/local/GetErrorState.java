package com.misset.opp.resolvable.local;

import com.intellij.openapi.project.Project;
import com.misset.opp.ttl.model.OppModelConstants;
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
        return Set.of(OppModelConstants.XSD_STRING_INSTANCE);
    }

    @Override
    public boolean isVoid() {
        return false;
    }
}
