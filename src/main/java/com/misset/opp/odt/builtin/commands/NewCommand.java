package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NewCommand extends BuiltInCommand {

    private static final List<String> PARAMETER_NAMES = List.of("iri", "graph");

    private NewCommand() {
    }

    public static final NewCommand INSTANCE = new NewCommand();

    @Override
    public String getName() {
        return "NEW";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        // create an instance from the class
        return OppModel.INSTANCE.toIndividuals(call.resolveSignatureArgument(0))
                .stream()
                .map(OntResource.class::cast)
                .collect(Collectors.toSet());
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        validateClassNameArgument(0, call, holder);
        validateNamedGraphArgument(1, call, holder);
    }

    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 1) {
            return Set.of(OppModelConstants.NAMED_GRAPH);
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
