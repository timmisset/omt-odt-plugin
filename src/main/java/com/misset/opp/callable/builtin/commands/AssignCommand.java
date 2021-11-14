package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
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
    protected Set<OntResource> resolveFrom(Call call) {
        return call.resolveSignatureArgument(0);
    }
}
