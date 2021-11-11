package com.misset.opp.callable.builtin.commands;

import com.misset.opp.odt.psi.impl.call.ODTCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class AssignCommand extends BuiltInCommand {
    private AssignCommand() { }
    public static final AssignCommand INSTANCE = new AssignCommand();
    @Override
    public String getName() {
        return "ASSIGN";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

    @Override
    protected Set<OntResource> resolveFrom(ODTCall call) {
        return call.resolveSignatureArgument(0);
    }
}
