package com.misset.opp.callable.builtin.commands;

import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class WarningCommand extends BuiltInCommand {
    private WarningCommand() { }
    public static final WarningCommand INSTANCE = new WarningCommand();

    @Override
    public String getName() {
        return "WARNING";
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
