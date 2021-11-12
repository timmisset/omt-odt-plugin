package com.misset.opp.callable.builtin.commands;

import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
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
    protected Set<OntResource> resolveFrom(ODTCall call) {
        return combineArgumentResources(call);
    }
}
