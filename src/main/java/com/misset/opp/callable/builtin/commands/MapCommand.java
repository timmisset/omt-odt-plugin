package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
import com.misset.opp.callable.local.LocalVariable;
import com.misset.opp.callable.local.LocalVariableTypeProvider;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MapCommand extends BuiltInCommand implements LocalVariableTypeProvider {
    private MapCommand() {
    }

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

    @Override
    public boolean providesTypeFor(String name) {
        return List.of("$value", "$index", "$array").contains(name);
    }

    @Override
    public Set<OntResource> getType(String name,
                                    Call call) {
        if (List.of("$value", "$array").contains(name)) {
            return call.resolveSignatureArgument(0);
        } else if ("$index".equals(name)) {
            return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
        }
        return Collections.emptySet();
    }
}
