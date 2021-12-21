package com.misset.opp.odt.builtin.commands;

import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class SerialCommand extends BuiltInCommand {
    private SerialCommand() { }
    public static final SerialCommand INSTANCE = new SerialCommand();

    @Override
    public String getName() {
        return "SERIAL";
    }

    @Override
    public int minNumberOfArguments() {
        return -1;
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return combineArgumentResources(call);
    }
}
