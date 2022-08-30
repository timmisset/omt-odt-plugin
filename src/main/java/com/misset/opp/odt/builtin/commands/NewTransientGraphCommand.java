package com.misset.opp.odt.builtin.commands;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;

public class NewTransientGraphCommand extends AbstractBuiltInCommand {
    private NewTransientGraphCommand() {
    }

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
        return OppModelConstants.NAMED_GRAPH;
    }

    @Override
    protected List<String> getParameters() {
        return Collections.emptyList();
    }
}
