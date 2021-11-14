package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
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
    protected Set<OntResource> resolveFrom(Call call) {
        return combineArgumentResources(call);
    }
}
