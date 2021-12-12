package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
import com.misset.opp.callable.local.CallableLocalVariableTypeProvider;
import com.misset.opp.callable.local.LocalVariable;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ForEachCommand extends BuiltInCommand implements CallableLocalVariableTypeProvider {
    private ForEachCommand() {
    }

    public static final ForEachCommand INSTANCE = new ForEachCommand();

    @Override
    public String getName() {
        return "FOREACH";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected Set<OntResource> resolveFrom(Call call) {
        return Collections.emptySet();
    }

    public List<LocalVariable> getLocalVariables(Call call, int argumentIndex) {
        if (argumentIndex != 1) {
            return Collections.emptyList();
        }
        return List.of(
                new LocalVariable("$value", "iterator value", call.resolveSignatureArgument(0)),
                new LocalVariable("$index", "iterator index", Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE)),
                new LocalVariable("$array", "all input values", call.resolveSignatureArgument(0))
        );
    }
}
