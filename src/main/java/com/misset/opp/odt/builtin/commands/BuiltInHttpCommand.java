package com.misset.opp.odt.builtin.commands;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public abstract class BuiltInHttpCommand extends AbstractBuiltInCommand {

    @Override
    public OntResource resolveSingle() {
        return OppModelConstants.getJsonObject();
    }

    @Override
    public Set<OntResource> getSecondReturnArgument() {
        return Set.of(OppModelConstants.getXsdIntegerInstance());
    }
}
