package com.misset.opp.odt.builtin.commands;

import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class LogCommand extends BuiltInCommand {
    private LogCommand() { }
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
}
