package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class SerialCommand extends BuiltInCommand {
    private SerialCommand() { }
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
    protected Set<OntResource> resolveFrom(Call call) {
        return combineArgumentResources(call);
    }
}
