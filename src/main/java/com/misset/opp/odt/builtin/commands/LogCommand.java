package com.misset.opp.odt.builtin.commands;

import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class LogCommand extends BuiltInCommand {

    private static final List<String> PARAMETER_NAMES = List.of("command");

    private LogCommand() {
    }

    public static final LogCommand INSTANCE = new LogCommand();

    @Override
    public String getName() {
        return "LOG";
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
