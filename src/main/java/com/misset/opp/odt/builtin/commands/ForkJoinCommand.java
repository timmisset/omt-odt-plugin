package com.misset.opp.odt.builtin.commands;

import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class ForkJoinCommand extends AbstractBuiltInCommand {

    private static final List<String> PARAMETER_NAMES = List.of("command");

    private ForkJoinCommand() {
    }

    public static final ForkJoinCommand INSTANCE = new ForkJoinCommand();

    @Override
    public String getName() {
        return "FORKJOIN";
    }

    @Override
    public int minNumberOfArguments() {
        return -1;
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return combineArgumentResources(call);
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
