package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public class IfCommand extends BuiltInCommand {
    private IfCommand() { }
    public static final IfCommand INSTANCE = new IfCommand();

    @Override
    public String getName() {
        return "IF";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    protected Set<OntResource> resolveFrom(Call call) {
        final HashSet<OntResource> resources = new HashSet<>();
        resources.addAll(call.resolveSignatureArgument(1));
        resources.addAll(call.resolveSignatureArgument(2));
        return resources;
    }
}
