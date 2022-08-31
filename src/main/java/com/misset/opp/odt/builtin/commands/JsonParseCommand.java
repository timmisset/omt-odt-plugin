
package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class JsonParseCommand extends AbstractBuiltInCommand {

    protected static final String DESCRIPTION = "Parse a JSON object to an Ontology class";
    private static final List<String> PARAMETER_NAMES = List.of("json", "class", "namedGraph");

    private JsonParseCommand() {
    }

    public static final JsonParseCommand INSTANCE = new JsonParseCommand();

    @Override
    public String getDescription(Project project) {
        return DESCRIPTION;
    }

    @Override
    public String getName() {
        return "JSON_PARSE";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 3;
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    public Set<OntResource> resolveFrom(Set<OntResource> resources,
                                        PsiCall call) {
        return call.resolveSignatureArgument(1).stream()
                .map(Objects::toString)
                .map(OppModel.getInstance()::toIndividuals)
                .flatMap(Collection::stream)
                .map(individual -> Set.of((OntResource) individual))
                .findFirst()
                .orElse(Collections.emptySet());
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        ArgumentValidator.validateJSONArgument(0, call, holder);
        ArgumentValidator.validateClassNameArgument(1, call, holder);
        ArgumentValidator.validateNamedGraphArgument(2, call, holder);
    }

    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.getJsonObject());
        } else if (index == 2) {
            return Set.of(OppModelConstants.getNamedGraph());
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
