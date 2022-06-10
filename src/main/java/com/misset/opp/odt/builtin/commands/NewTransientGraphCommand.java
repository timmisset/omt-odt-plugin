package com.misset.opp.odt.builtin.commands;

import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntResource;

public class NewTransientGraphCommand extends BuiltInCommand {
    private NewTransientGraphCommand() { }
    public static final NewTransientGraphCommand INSTANCE = new NewTransientGraphCommand();

    @Override
    public String getName() {
        return "NEW_TRANSIENT_GRAPH";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.NAMED_GRAPH;
    }
}
