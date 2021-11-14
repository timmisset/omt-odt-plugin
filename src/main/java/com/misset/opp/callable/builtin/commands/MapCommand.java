package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MapCommand extends BuiltInCommand {
    private MapCommand() { }
    public static final MapCommand INSTANCE = new MapCommand();
    public static final List<LocalVariable> variables = new ArrayList<>();
    static {
        variables.add(new LocalVariable("$value", "the value from the collection"));
        variables.add(new LocalVariable("$index", "the index of the value in the collection"));
        variables.add(new LocalVariable("$array", "the entire values collection"));
    }

    @Override
    public String getName() {
        return "MAP";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public List<LocalVariable> getLocalVariables() {
        return variables;
    }

    @Override
    protected Set<OntResource> resolveFrom(Call call) {
        return call.resolveSignatureArgument(0);
    }
}
