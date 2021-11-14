package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
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
    protected Set<OntResource> resolveFrom(Call call) {
        return call.resolveSignatureArgument(0);
    }
}
