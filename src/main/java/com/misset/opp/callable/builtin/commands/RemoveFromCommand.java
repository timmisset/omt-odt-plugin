package com.misset.opp.callable.builtin.commands;

import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class RemoveFromCommand extends BuiltInCommand {
    private RemoveFromCommand() { }
    public static final RemoveFromCommand INSTANCE = new RemoveFromCommand();

    @Override
    public String getName() {
        return "REMOVE_FROM";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected Set<OntResource> resolveFrom(ODTCall call) {
        return call.resolveSignatureArgument(0);
    }
}
