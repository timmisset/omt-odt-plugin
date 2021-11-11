package com.misset.opp.callable.builtin.commands;

import com.misset.opp.odt.psi.impl.call.ODTCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class MoveToGraphCommand extends BuiltInCommand {
    private MoveToGraphCommand() { }
    public static final MoveToGraphCommand INSTANCE = new MoveToGraphCommand();

    @Override
    public String getName() {
        return "MOVE_TO_GRAPH";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected Set<OntResource> resolveFrom(ODTCall call) {
        return call.resolveSignatureArgument(0);
    }
}
