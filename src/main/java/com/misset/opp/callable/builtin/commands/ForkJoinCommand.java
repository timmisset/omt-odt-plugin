package com.misset.opp.callable.builtin.commands;

import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class ForkJoinCommand extends BuiltInCommand {
    private ForkJoinCommand() { }
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
    protected Set<OntResource> resolveFrom(ODTCall call) {
        return combineArgumentResources(call);
    }
}
