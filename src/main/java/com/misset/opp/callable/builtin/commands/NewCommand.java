package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class NewCommand extends BuiltInCommand {
    private NewCommand() { }
    public static final NewCommand INSTANCE = new NewCommand();

    @Override
    public String getName() {
        return "NEW";
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
    protected Set<OntResource> resolveFrom(Call call) {
        // create an instance from the class
        return OppModel.INSTANCE.toIndividuals(call.resolveSignatureArgument(0));
    }
}
