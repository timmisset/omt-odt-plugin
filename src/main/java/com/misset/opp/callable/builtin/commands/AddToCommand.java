package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class AddToCommand extends BuiltInCommand {
    private AddToCommand() { }
    public static final AddToCommand INSTANCE = new AddToCommand();

    @Override
    public String getName() {
        return "ADD_TO";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected Set<OntResource> resolveFrom(Call call) {
        return combineArgumentResources(call);
    }
}
