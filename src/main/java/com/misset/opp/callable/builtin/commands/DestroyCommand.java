package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.Set;

public class DestroyCommand extends BuiltInCommand {
    private DestroyCommand() { }
    public static final DestroyCommand INSTANCE = new DestroyCommand();

    @Override
    public String getName() {
        return "DESTROY";
    }

    @Override
    protected Set<OntResource> resolveFrom(Call call) {
        return Collections.emptySet();
    }
}
