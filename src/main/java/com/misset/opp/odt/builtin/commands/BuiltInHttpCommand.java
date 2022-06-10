package com.misset.opp.odt.builtin.commands;

import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public abstract class BuiltInHttpCommand extends BuiltInCommand {

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.JSON_OBJECT;
    }

    @Override
    public Set<OntResource> getSecondReturnArgument() {
        return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
    }
}
