
package com.misset.opp.callable.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class JsonParseCommand extends BuiltInCommand {
    private JsonParseCommand() {
    }

    public static final JsonParseCommand INSTANCE = new JsonParseCommand();

    @Override
    public String getDescription(String context) {
        return "Parse a JSON object to an Ontology class";
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
                                        Call call) {
        return call.resolveSignatureArgument(1).stream()
                .map(Objects::toString)
                .map(OppModel.INSTANCE::getClassIndividuals)
                .flatMap(Collection::stream)
                .map(individual -> Set.of((OntResource) individual))
                .findFirst()
                .orElse(Collections.emptySet());
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        validateJSONArgument(0, call, holder);
        validateClassNameArgument(1, call, holder);
        validateNamedGraphArgument(2, call, holder);
    }
}
