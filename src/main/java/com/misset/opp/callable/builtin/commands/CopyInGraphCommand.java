package com.misset.opp.callable.builtin.commands;

import com.misset.opp.odt.psi.impl.call.ODTCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class CopyInGraphCommand extends BuiltInCommand {
    private CopyInGraphCommand() {
    }

    public static final CopyInGraphCommand INSTANCE = new CopyInGraphCommand();

    @Override
    public String getName() {
        return "COPY_IN_GRAPH";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    protected Set<OntResource> resolveFrom(ODTCall call) {
        return call.resolveSignatureArgument(0);
    }
}
