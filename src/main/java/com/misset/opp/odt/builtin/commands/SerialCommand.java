package com.misset.opp.odt.builtin.commands;

import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class SerialCommand extends AbstractBuiltInCommand {

    private static final List<String> PARAMETER_NAMES = List.of("command");

    private SerialCommand() {
    }

    public static final SerialCommand INSTANCE = new SerialCommand();

    @Override
    public String getName() {
        return "SERIAL";
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
