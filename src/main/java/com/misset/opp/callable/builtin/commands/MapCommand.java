package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
import com.misset.opp.callable.local.CallableLocalVariableTypeProvider;
import com.misset.opp.callable.local.LocalVariable;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MapCommand extends BuiltInCommand implements CallableLocalVariableTypeProvider {
    private MapCommand() {
    }

    public static final MapCommand INSTANCE = new MapCommand();


    @Override
    public String getName() {
        return "MAP";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected Set<OntResource> resolveFrom(Call call) {
        return call.resolveSignatureArgument(0);
    }

    @Override
    public List<LocalVariable> getLocalVariables(Call call, int argumentIndex) {
        if (argumentIndex != 1) {
            return Collections.emptyList();
        }
        return List.of(
                new LocalVariable("$value", "iterator value", call.resolveSignatureArgument(0)),
                new LocalVariable("$index", "iterator index", call.resolveSignatureArgument(0)),
                new LocalVariable("$array", "all input values", call.resolveSignatureArgument(0))
        );
    }
}
