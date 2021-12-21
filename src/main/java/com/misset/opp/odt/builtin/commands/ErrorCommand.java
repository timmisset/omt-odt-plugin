package com.misset.opp.odt.builtin.commands;

import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class ErrorCommand extends BuiltInCommand {
    private ErrorCommand() { }
    public static final ErrorCommand INSTANCE = new ErrorCommand();

    @Override
    public String getName() {
        return "ERROR";
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
