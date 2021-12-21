
package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

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
                                        PsiCall call) {
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

    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModel.INSTANCE.JSON_OBJECT);
        } else if (index == 2) {
            return Set.of(OppModel.INSTANCE.NAMED_GRAPH);
        }
        return null;
    }
}
